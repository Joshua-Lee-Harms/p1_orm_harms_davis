package com.revature;

import com.revature.exceptions.MissingAnnotationException;
import com.revature.repositories.Repository;
import com.revature.util.ConnectionFactory;
import com.revature.util.TestModel;

import java.sql.Connection;
import java.sql.SQLException;


public class Driver {
	
	public static void main(String[] args) {
		Connection conn = ConnectionFactory.getConnection();
		Repository<Object> repo = new Repository();
		TestModel tm = new TestModel();
		
		try {
			repo.initializeTable(tm);
		} catch (SQLException | MissingAnnotationException e) {
			e.printStackTrace();
		}
	}
}
