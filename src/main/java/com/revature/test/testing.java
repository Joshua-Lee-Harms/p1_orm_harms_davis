package com.revature.test;

import com.revature.exceptions.MissingAnnotationException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Entity;
import com.revature.repositories.Repository;
import com.revature.services.StatementCreator;
import com.revature.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class testing {
	
	public static void main(String[] args) throws ResourceNotFoundException {
		
		// Entity now inherits Repository
		
		Entity entity = new Entity();
		entity.setId(1);
		entity.setName("name1");
		entity.setSomeNum(1.1);
		entity.setOtherNum(11);
		
		try {
			entity.initializeTable(entity);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (MissingAnnotationException e) {
			e.printStackTrace();
		}
		/*
		entity.addItem(entity);
		
		entity.setId(2);
		entity.setName("name2");
		entity.setSomeNum(2.2);
		entity.setOtherNum(22);
		entity.addItem(entity);
		
		//System.out.println("get 1:\t"+ entity.getItem(1) +"\n");
		System.out.println("getall:\t"+ entity.getAll() );
		entity.deleteItem(1);
		entity.deleteItem(2);
		*/
		
		// tests for Repository class. works for adding things to postgres
		//Repository<Entity> repo = new Repository();
		//repo.addItem(entity);
		//repo.deleteItem(1);
		//System.out.println("getItem:\t" + repo.getItem(1) );
		//System.out.println();
		
		// tests for StatementCreator class
		//StatementCreator<Object> sc = new StatementCreator<>();
		//System.out.println("\nStatement Creator:");
		// Tests for initialize table method
		//try { sc.buildInitialTable(entity); }
		//catch (MissingAnnotationException e) { e.printStackTrace(); }
		
		//System.out.println( sc.create(entity) );
		//System.out.println( sc.delete(entity) );
		//System.out.println("\ncurrently Table name:" + sc.translate() );	//successful print of table name
		
		
		
		
	}
}
