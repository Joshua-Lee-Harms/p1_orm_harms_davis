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
	public void initializeTable(Object o) throws SQLException, MissingAnnotationException {
		PreparedStatement drop = conn.prepareStatement("drop table if exists " +ReflectInfo.getTableName(o)+ ";");
		drop.executeUpdate();
		
		StatementCreator<Object> sc = new StatementCreator<>();
		String sql = sc.buildInitialTable(o);

		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate();
	}
	
	public Object addItem(Object o) {
		
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
				SQLException | IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getAll(Object o) throws InstantiationException, IllegalAccessException {
		
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

	public void deleteItem(int id, Object o) {
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
	
	public void update(Object object, int id, String updateField, T updateValue) throws ResourceNotFoundException {
		StatementCreator<Object> sc = new StatementCreator<>();
		// this.updatingField =updatingField.getAnnotation(Column.class).columnName();
		Field[] fields = object.getClass().getDeclaredFields();
		//String[] colNames = new String[ReflectInfo.getFieldLength(object)];
		List<String> colNames = new LinkedList<>();
		String sql;
		
		for (int i= 0; i < ReflectInfo.getFieldLength(object); i++) {
			fields[i].setAccessible(true);
			colNames.add(fields[i].getAnnotation(Column.class).name());
			fields[i].setAccessible(false);
		}
		if (colNames.contains(updateField))
			sql = sc.update(object, id, updateField, updateValue);
		else
			throw new ResourceNotFoundException("Column/Field not found");
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	// Helper Methods
	private Object buildItem(ResultSet rs, Object o) throws SQLException, IllegalAccessException, InstantiationException {
		
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
