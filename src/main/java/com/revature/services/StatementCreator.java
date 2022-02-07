package com.revature.services;

import com.revature.annotations.Column;
import com.revature.annotations.ForeignKey;
import com.revature.annotations.Table;
import com.revature.exceptions.MissingAnnotationException;
import com.revature.repositories.Repository;
import com.revature.util.ReflectInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class StatementCreator<T> {
	
	private String tableName;
	private String statement;
	
	public StatementCreator(){}
	
	// UPDATE METHOD
	
	public String buildInitialTable(T object) throws MissingAnnotationException {
		if (!object.getClass().isAnnotationPresent(Table.class)){
			throw new MissingAnnotationException("@Table annotation is missing.");
		}
		String tableName = ReflectInfo.getTableName(object);
		String pk = ReflectInfo.getFieldNames(object)[0];
		
		Field[] fields = object.getClass().getDeclaredFields();
		List<String> foreignKey = new ArrayList<>();
		List<String> cols = new ArrayList<>();
		
		
		for (Field f : fields) {
			String colName, type, constraint;
			String columnString = "";
			
			if (f.isAnnotationPresent(Column.class)) {
				colName = f.getName();
				type = f.getAnnotation(Column.class).type().toString();
				
				//ADD OTHER TYPES
				if (type.equals("VARCHAR")) columnString = " " +colName+ " " +type+ "(20), ";
				else if (type.equals("DOUBLE")) columnString = colName+ " " +type+ " precision, ";
				else if (type.equals("INTEGER")) columnString = colName+ " " +type+ ", ";
				else if (type.equals("BOOLEAN") || type.equals("BOOL")) columnString = colName+ " " +type+ ", ";
				cols.add(columnString);
			} else {
				throw new MissingAnnotationException(f + " missing annotation @Column");
			}
			
			if (f.isAnnotationPresent(ForeignKey.class)) {
				String fkTable = f.getAnnotation(ForeignKey.class).foreignKey();
				// WILL NEED TO FIX VALUE BELOW IF FOREIGN KEY IS IMPLEMENTED
				String fkColName = f.getName();
				
				String fkString = "foreign Key (" + colName + ") references " + fkTable + " (" + fkColName + "), ";
				foreignKey.add(fkString);
			}
		}
		String createTableSql = "create table if not exists " +tableName+ " ( ";
		
		for (int i=0; i<cols.size(); i++ ){
			if (i==0) createTableSql += cols.get(i).split(" ")[0] + " serial primary key, ";
			else { createTableSql += cols.get(i); }
		}
		for (String fk: foreignKey){
			createTableSql += fk;
		}
		
		createTableSql = createTableSql.substring(0, createTableSql.length()-2);
		createTableSql += ");";
		
		return  createTableSql;
	}
	
	public String create(T object){
		
		tableName = ReflectInfo.getTableName(object);
		int fieldLength = ReflectInfo.getFieldLength(object);
		
		Field[] fields;
		fields = object.getClass().getDeclaredFields();
		String[] colNames = new String[fieldLength];
		
		for (int i= 0; i < fieldLength; i++) {
			fields[i].setAccessible(true);
			colNames[i] = fields[i].getAnnotation(Column.class).name();
		}
		
		StringBuilder cols = new StringBuilder();
		StringBuilder vals = new StringBuilder();
		
		cols.append(colNames[0]).append(",");
		for (int i=1; i<fieldLength; i++){
			cols.append(colNames[i]).append(",");
			vals.append("?,");
		}
		vals = new StringBuilder(vals.substring(0, vals.length()-1));
		
		statement = "insert into "+ tableName+ " ("+ cols.substring(0, cols.length()-1)
				+") "+ "values (default,"+ vals + ") returning *;";
		
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
		tableName = ReflectInfo.getTableName(object);
		statement = "select * from " + tableName + ";";
		
		return statement;
	}
	
	public String update(int id, T newObject) throws IllegalAccessException {
		
		tableName = ReflectInfo.getTableName(newObject);
		int fieldLength = ReflectInfo.getFieldLength(newObject);
		
		Field[] fields = newObject.getClass().getDeclaredFields();
		String[] colNames = new String[fieldLength];
		Object[] fieldValues = ReflectInfo.getFieldValues(newObject);
		String type;
		
		statement = "update "+ tableName+ " set ";
		
		for (int i= 0; i < fieldLength; i++) {
			fields[i].setAccessible(true);
			colNames[i] = fields[i].getAnnotation(Column.class).name();
			type = fields[i].getType().getSimpleName();
			
			if (i != 0) {
				if (type.equalsIgnoreCase("string"))
					statement += colNames[i] + "= '" + fieldValues[i] + "',";
				else
					statement += colNames[i] + "= " + fieldValues[i] + ",";
				fields[i].setAccessible(false);
			}
		}
		
		statement = statement.substring(0, statement.length()-1) +"";
		statement += " where " +colNames[0]+ "= " +id+ ";";
		System.out.println(statement);
		return statement;
	}
	
}
