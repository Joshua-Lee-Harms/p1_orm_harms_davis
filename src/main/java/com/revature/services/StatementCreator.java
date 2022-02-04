package com.revature.services;

import com.revature.annotations.Column;
import com.revature.annotations.Table;
import com.revature.util.ConnectionFactory;
import com.revature.util.ReflectInfo;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Arrays;

public class StatementCreator<T> {
	
	Connection conn = ConnectionFactory.getConnection();
	public Class<T> table;
	private T value;
	private T object;
	private String tableName;
	private String statement;

	public StatementCreator(){}

	public void buildTable(Object o) {}

	
	public String create(T object){
		this.object = object;
		tableName = ReflectInfo.getTableName(object);

		int flength = ReflectInfo.getFieldLength(object);
		Field[] fields;
		String[] colNames = new String[flength];
		fields = object.getClass().getDeclaredFields();
		
		for (int i = 0; i < flength; i++) {
			fields[i].setAccessible(true);
			colNames[i] = fields[i].getAnnotation(Column.class).name();
		}
		
		StringBuilder cols = new StringBuilder();
		StringBuilder vals = new StringBuilder();

		cols.append(colNames[0]).append(",");
		for (int i = 1; i< flength; i++){
			cols.append(colNames[i]).append(",");
			vals.append("?,");
		}
		vals = new StringBuilder(vals.substring(0, vals.length()-1));
		
		statement = "insert into "+ tableName+ " ("+ cols.substring(0, cols.length()-1)
					+") "+ "values ( default,"+ vals + ") returning *;";
		
		return statement;
	}
	
	public String delete(T object){
		tableName = ReflectInfo.getTableName(object);
		String[] colNames = new String[ReflectInfo.getFieldLength(object)];
		Field[] fields = object.getClass().getDeclaredFields();
		
		for (int i= 0; i < ReflectInfo.getFieldLength(object); i++) {
			fields[i].setAccessible(true);
			colNames[i] = fields[i].getAnnotation(Column.class).name();
		}
		statement = "delete from " +tableName+ " where " +colNames[0]+ " = ?;";
		
		return statement;
	}
	
	public String read(T object){
		this.object = object;
		tableName = ReflectInfo.getTableName(object);
		
		String[] colNames = new String[ReflectInfo.getFieldLength(object)];
		Field[] fields = object.getClass().getDeclaredFields();
		
		for (int i= 0; i < ReflectInfo.getFieldLength(object); i++) {
			fields[i].setAccessible(true);
			colNames[i] = fields[i].getAnnotation(Column.class).name();
		}
		
		statement = "select * from " + tableName + " where " +colNames[0]+ " = ?;";
		
		return statement;
	}
	
	public String readAll(T object){
		this.object = object;
		tableName = ReflectInfo.getTableName(object);
		
		statement = "select * from " + tableName + ";";
		
		return statement;
	}
	
	public String update(T object){
		return "";
	}

	public String buildInitialTable(T tRepository) {
		return "";
	}
}
