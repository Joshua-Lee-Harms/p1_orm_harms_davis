package com.revature.repositories;

import com.revature.exceptions.MissingAnnotationException;
import com.revature.services.StatementCreator;
import com.revature.util.ConnectionFactory;
import com.revature.util.ReflectInfo;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.sql.ResultSet.CONCUR_UPDATABLE;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;

// Repository must be inherited by models
public class Repository<T> {

	Connection conn;// = ConnectionFactory.getConnection();
	private Class<T> persistClass;

	public Repository(Connection conn) {
		this.conn = conn;
	}
	
	public Connection getConn(){ return conn; }
	public void setConn(Connection conn){ this.conn = conn; }
	public Repository(T pc){ this.persistClass = (Class<T>) pc; }
	public Class<T> getPersistClass(){ return persistClass; }
	
	
	// INITIALIZE TABLE
	public void initializeTable(Object o) throws SQLException, MissingAnnotationException {
		PreparedStatement drop = conn.prepareStatement("drop table if exists " +ReflectInfo.getTableName(o)+ ";");
		drop.executeUpdate();
		
		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.buildInitialTable(o);

		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate();
	}
	
	public Object addItem(Object o) {
		//String sql = "INSERT INTO entity VALUES (default,?,?,?) RETURNING *";
		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.create(o);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			Object[] fieldValues = ReflectInfo.getFieldValues(o);
			
			for (int i=0; i < ReflectInfo.getFieldLength(o) - 1; i++){
				//System.out.println( fieldValues[i] +":\t"+  fieldValues[i].getClass().getSimpleName() );
				ps.setObject(i+1, fieldValues[i+1]);
			}
			//ps.setString(1, entity.getName());
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) return buildItem(rs, o);
		}
		catch (SQLException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getItem(int id,Object o) {
		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.read(o);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setObject(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return buildItem(rs, o);
			}

		} catch (
				SQLException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// <? extends Connection> OR <? extends Repository>
	public Object getAll(Object o) throws InstantiationException, IllegalAccessException {
		
		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.readAll(o);
		
		
		try {
			
			PreparedStatement ps = conn.prepareStatement(sql, TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);
			//PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			//Statement st = conn.createStatement(TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);
			//ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			int colCount = md.getColumnCount();
			
			Class<?>[] params = new Class[colCount];
			List<Object> data = new LinkedList<>();
			List<Object> objects = new LinkedList<>();
			
			//System.out.println(rs.last() +":\t" +rs.getRow());
			//rs.beforeFirst();
			while(rs.next()) {
				for (int i = 1; i <= colCount; i++) {
					params[i-1] =  rs.getObject(i).getClass();
					data.add( rs.getObject(i) );
				}
				objects.add( buildItem(rs, o) );
				System.out.println(objects);
				
				// !!!!! need to convert params to primitive type classes !!!!!
				Constructor<?> constructor = o.getClass().getConstructor( params );
				return constructor.newInstance(data.toArray());
			}
			//return data;
		} catch (
				SQLException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteItem(int id, Object o) {
		StatementCreator<Object> sc = new StatementCreator<>();
		// since Entity now inherits Repository, 'this' refers to Entity class
		String sql = sc.delete(o);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setObject(1, id);
			ps.executeUpdate();
			ps.getGeneratedKeys();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Helper Methods

	public String getObjectType() {
		return this.getClass().getTypeName();
	}

	private Object buildItem(ResultSet rs, Object o) throws SQLException, IllegalAccessException {

		// should this be an instance of Repo instead of Object?
		Field[] fields = o.getClass().getDeclaredFields();
		Object ob = new Object();

		for (Field f : fields) {
			f.setAccessible(true);
			// (object whose field should be modified, new value for the field of obj being modified)
			f.set(o, rs.getObject(f.getName()));
			f.setAccessible(false);
		}

		return o;
	}
	
}
