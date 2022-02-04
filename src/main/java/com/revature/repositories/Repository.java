package com.revature.repositories;

import com.revature.services.StatementCreator;
import com.revature.util.ConnectionFactory;
import com.revature.util.ReflectInfo;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

// Repository must be inherited by models
public class Repository {

	StatementCreator<Object> sc = new StatementCreator<>();

	Connection conn = ConnectionFactory.getConnection();
	private List<Object> data = new LinkedList<>();

	public Repository() {}





	// INITIALIZE TABLE
//	public void initializeTable() throws SQLException, MissingAnnotationException {
//		StatementCreator<Object> sc = new StatementCreator<>();
//		String sql = sc.buildInitialTable(this);
//		//Connection conn = repo.getConn();
//
//		PreparedStatement ps = conn.prepareStatement(sql);
//		ps.executeUpdate();
//	}

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

			if (rs.next()) return buildItem(rs);
		}
		catch (SQLException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getItem(int id) {
		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.read(this);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setObject(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return buildItem(rs);
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
	public List<Object> getAll(Class<?> clazz) throws InstantiationException, IllegalAccessException {

		Object obj = clazz.newInstance();	//

		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.readAll(this);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int colCount = rs.getMetaData().getColumnCount();
//			System.out.println(rs.getMetaData().getColumnCount());

			int numFields = clazz.getDeclaredFields().length;

			while (rs.next()) {
				//data.add(buildItem(rs));


				for (int i = 1; i <= colCount; i++) {
					data.add( rs.getObject(i) );
				}
			}
			//System.out.println("data:\t" + data );

			return data;

		} catch (
				SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteItem(int id) {
		StatementCreator<Object> sc = new StatementCreator<>();
		// since Entity now inherits Repository, 'this' refers to Entity class
		String sql = sc.delete(this);

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

	private Object buildItem(ResultSet rs) throws SQLException, IllegalAccessException {

		// should this be an instance of Repo instead of Object?
		Field[] fields = this.getClass().getDeclaredFields();
		Object ob = new Object();
		ob = this;

		for (Field f : fields) {
			f.setAccessible(true);
			// (object whose field should be modified, new value for the field of obj being modified)
			f.set(this, rs.getObject(f.getName()));
			f.setAccessible(false);
		}

		return ob;
	}
}
