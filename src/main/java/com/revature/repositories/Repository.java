package com.revature.repositories;

import com.revature.annotations.Column;
import com.revature.exceptions.MissingAnnotationException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.services.StatementCreator;
import com.revature.util.ConnectionFactory;
import com.revature.util.ReflectInfo;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;


public class Repository<T> {

	Connection conn = ConnectionFactory.getConnection();

	public Repository(){}
	
	// INITIALIZE TABLE
	public void initializeTable(T o) throws SQLException, MissingAnnotationException {
		PreparedStatement drop = conn.prepareStatement("drop table if exists " +ReflectInfo.getTableName(o)+ ";");
		drop.executeUpdate();
		
		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.buildInitialTable(o);

		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate();
	}
	
	public Object addItem(T o) {
		
		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.create(o);

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			Object[] fieldValues = ReflectInfo.getFieldValues(o);
			
			for (int i=0; i < ReflectInfo.getFieldLength(o) - 1; i++){
				ps.setObject(i+1, fieldValues[i+1]);
			}
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) return buildItem(rs, o);
		}
		catch (SQLException | IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object getItem(int id,T o) {
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
				SQLException | IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getAll(T o) throws InstantiationException, IllegalAccessException {
		
		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.readAll(o);
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			LinkedList<Object> objects = new LinkedList<>();
			
			while(rs.next()) {
				objects.addLast( buildItem(rs, o) );
			}
			return objects;
			
		} catch (
				SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteItem(int id, T o) {
		StatementCreator<Object> sc = new StatementCreator<>();
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
	
	public void update( int id, T newObject) throws IllegalAccessException {
		
		StatementCreator<Object> sc = new StatementCreator<>();
		Field[] fields = newObject.getClass().getDeclaredFields();
		List<String> colNames = new LinkedList<>();
		
		String sql;
		
		for (int i= 0; i < ReflectInfo.getFieldLength(newObject); i++) {
			fields[i].setAccessible(true);
			colNames.add(fields[i].getAnnotation(Column.class).name());
			fields[i].setAccessible(false);
		}
		
		sql = sc.update(id, newObject);
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	// Helper Methods
	private Object buildItem(ResultSet rs, T o) throws SQLException, IllegalAccessException, InstantiationException {
		
		Field[] fields = o.getClass().getDeclaredFields();
		Object ob2 = o.getClass().newInstance();
		
		for (Field f : fields) {
			f.setAccessible(true);
			f.set(ob2 , rs.getObject(f.getName()));
			f.setAccessible(false);
		}
		
		return ob2;
	}
	
}
