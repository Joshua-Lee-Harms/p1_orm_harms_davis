package com.revature.util;

import com.revature.annotations.Table;
import java.lang.reflect.Field;

public class ReflectInfo {
	
	ReflectInfo(){}
	
	public static String getTableName(Object o){
		return o.getClass().getAnnotation(Table.class).tableName();
	}
	
	public static int getFieldLength(Object o){
		return o.getClass().getDeclaredFields().length;
	}
	
	public static String[] getFieldNames(Object o){
		Field[] fields = o.getClass().getDeclaredFields();
		String[] fieldNames = new String[getFieldLength(o)];
		
		for (int i=0; i<getFieldLength(o); i++){
			fieldNames[i] = fields[i].getName();
		}
		
		return fieldNames;
	}
	
	public static Object[] getFieldValues(Object o) throws IllegalAccessException {
		Field[] fields = o.getClass().getDeclaredFields();
		Object[] fieldValues = new Object[getFieldLength(o)];
		
		for (int i=0; i< getFieldLength(o); i++){
			fields[i].setAccessible(true);
			
			fieldValues[i] = fields[i].get(o);
		}
		return fieldValues;
	}
	
	
}
