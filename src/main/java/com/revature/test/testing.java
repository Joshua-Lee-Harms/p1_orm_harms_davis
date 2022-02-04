package com.revature.test;

import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Entity;
import com.revature.repositories.Repository;
import com.revature.services.StatementCreator;
import com.revature.util.ConnectionFactory;

import java.sql.Connection;

public class testing {
	
	public static void main(String[] args) throws ResourceNotFoundException {
		
		// Entity now inherits Repository
		Entity e = new Entity();
		e.setName("name");
		e.setSomeNum(1.5);
		e.setOtherNum(22);

		Connection conn = ConnectionFactory.getConnection();

//		e.addItem(e);

		e.addItem(e);










		
	}
}
