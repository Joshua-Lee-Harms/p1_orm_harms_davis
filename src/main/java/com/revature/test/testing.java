package com.revature.test;

import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Entity;
import com.revature.models.Entity2;
import com.revature.repositories.Repository;
import com.revature.services.StatementCreator;
import com.revature.util.ConnectionFactory;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

public class testing {
	
	public static void main(String[] args) throws ResourceNotFoundException, InstantiationException, IllegalAccessException {
		
		// Entity now inherits Repository
		Entity e = new Entity();
//		e.setName("name");
//		e.setSomeNum(1.5);
//		e.setOtherNum(22);


		 List<Object> list = e.getAll(Entity.class);


		System.out.println(e2.toString());











		
	}
}
