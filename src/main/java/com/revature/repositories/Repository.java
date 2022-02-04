package com.revature.repositories;

import com.revature.exceptions.MissingAnnotationException;
import com.revature.services.StatementCreator;
import com.revature.util.ConnectionFactory;
import com.revature.util.ReflectInfo;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// Repository must be inherited by models
public class Repository {

	Connection conn;// = ConnectionFactory.getConnection();

	public Repository() {
		this.conn = ConnectionFactory.getConnection();
	}
	
	
	// INITIALIZE TABLE
	public void initializeTable(Object o) throws SQLException, MissingAnnotationException {
		PreparedStatement drop = conn.prepareStatement("drop table if exists " +ReflectInfo.getTableName(o)+ ";");
		drop.executeUpdate();
		
		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.buildInitialTable(o);

		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate();
	}

	// should this be Object or T ???
	public Object addItem(Object o) {
		//String sql = "INSERT INTO entity VALUES (default,?,?,?) RETURNING *";
		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.create(o);
		//System.out.println(sql);

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

	/*Fixme - Must Return List of the Class type passed in, should be a way to pass enough info in through the param
	* Fixme - via reflection to reconstruct a list of concrete objects from the stream of column data
	* Fixme -
	* Fixme -	Return List<Object> ?
	* Fixme -
	*/
	// <? extends Connection> OR <? extends Repository>
	public Object getAll(Object o, Connection c) throws InstantiationException, IllegalAccessException {

		//Object obj = clazz.newInstance();	//

		StatementCreator<Object> sc = new StatementCreator<>();
		
		// POSSIBLY PASSING IN WRONG INSTANCE/VALUE. RS.NEXT() NOT LOOPING
		String sql = sc.readAll(o);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			int colCount = md.getColumnCount();
			
			Class[] params = new Class[colCount];
			List<Object> data = new LinkedList<>();
			//int numFields = clazz.getDeclaredFields().length;
			
			while (rs.next()) {
				for (int i = 1; i <= colCount; i++) {
					params[i-1] =  rs.getObject(i).getClass() ; // class java.lang.Integer ...
					
					data.add(  rs.getObject(i)  );
					
				}
				// need to convert params to primitive type classes
				//System.out.println(Arrays.toString(params));
				Constructor<?> constructor = o.getClass().getConstructor( params );
				return constructor.newInstance(data.toArray());
				//data.add(buildItem(rs, c));
			}
			//System.out.println("data:\t" + data );

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
