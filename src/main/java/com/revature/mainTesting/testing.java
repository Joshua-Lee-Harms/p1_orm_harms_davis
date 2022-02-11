package com.revature.mainTesting;

import com.revature.exceptions.MissingAnnotationException;
import com.revature.exceptions.ResourceNotFoundException;
import com.revature.repositories.Repository;
import com.revature.util.TestModel;

import java.sql.SQLException;

public class testing {
	
	public static void main(String[] args) throws ResourceNotFoundException {
		
		// Entity no longer inherits Repository
		Repository<Object> repo = new Repository<>();
		
		TestModel tm = new TestModel();
		tm.setId(1);
		tm.setName("name1");
		tm.setSomeNum(1.1);
		tm.setOtherNum(11);
		
		try {
			repo.initializeTable(tm);
		} catch (SQLException | MissingAnnotationException e) {
			e.printStackTrace();
		}
		
		repo.addItem(tm);
		
		TestModel t2 = new TestModel();
		t2.setId(2);
		t2.setName("name2");
		t2.setSomeNum(2.2);
		t2.setOtherNum(22);
		repo.addItem(t2);
		
		//System.out.println("get 2:\t"+ repo.getItem(2, tm) +"\n");
		try {
			//System.out.println("getAll:\t"+ repo.getAll( tm, ConnectionFactory.getConnection() ));
			//System.out.println( repo.getAll(t2) );
			System.out.println( repo.getAll(tm) );
			
		} catch (InstantiationException | IllegalAccessException | NullPointerException e) {
			e.printStackTrace();
		} finally {
			//repo.deleteItem(1, tm);
			//repo.deleteItem(2, tm);
		}
		
		TestModel up = new TestModel();
		up.setId(1);
		up.setName("Anthony");
		up.setSomeNum(5.55);
		up.setOtherNum(55);
		try {
			repo.update(up.getId(), up);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		//repo.update(tm, 1, "name", "anthony");
		//repo.update(tm, 1, "other_num", 55) ;
		// tests for Repository class. works for adding things to postgres
		//Repository<Entity> repo = new Repository();
		//repo.addItem(tm);
		//repo.deleteItem(1);
		//System.out.println("getItem:\t" + repo.getItem(1) );
		//System.out.println();
		
		// tests for StatementCreator class
		//StatementCreator<Object> sc = new StatementCreator<>();
		//System.out.println(sc.update(t2, 1, "name", "anthony"));
		//System.out.println("\nStatement Creator:");
		// Tests for initialize table method
		//try { sc.buildInitialTable(tm); }
		//catch (MissingAnnotationException e) { e.printStackTrace(); }
		
		//System.out.println( sc.create(tm) );
		//System.out.println( sc.delete(tm) );
		//System.out.println("\ncurrently Table name:" + sc.translate() );	//successful print of table name
		
	}
}
