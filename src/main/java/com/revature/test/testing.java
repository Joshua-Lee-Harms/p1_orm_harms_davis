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
		
		// Entity no longer inherits Repository
		Connection conn = ConnectionFactory.getConnection();
		Repository repo = new Repository(conn);
		
		Entity entity = new Entity();
		entity.setId(1);
		entity.setName("name1");
		entity.setSomeNum(1.1);
		entity.setOtherNum(11);
		
		try {
			repo.initializeTable(entity);
		} catch (SQLException | MissingAnnotationException e) {
			e.printStackTrace();
		}
		
		repo.addItem(entity);
		
		Entity e2 = new Entity();
		e2.setId(2);
		e2.setName("name2");
		e2.setSomeNum(2.2);
		e2.setOtherNum(22);
		 repo.addItem(e2);
		
		//System.out.println("get 2:\t"+ repo.getItem(2, entity) +"\n");
		try {
			//System.out.println("getAll:\t"+ repo.getAll( entity, ConnectionFactory.getConnection() ));
			repo.getAll(e2);
			
		} catch (InstantiationException | IllegalAccessException | NullPointerException e) {
			e.printStackTrace();
		} finally {
			//repo.deleteItem(1, entity);
			//repo.deleteItem(2, entity);
		}
		
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
