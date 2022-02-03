package com.revature.test;

import com.revature.exceptions.ResourceNotFoundException;
import com.revature.models.Entity2;
import com.revature.repositories.Repository;
import com.revature.services.StatementCreator;
import com.revature.util.ConnectionFactory;

import java.sql.Connection;

public class testing {
	
	public static void main(String[] args) throws ResourceNotFoundException {
		
		// Entity now inherits Repository
		Entity entity = new Entity();
		entity.setId(1);
		entity.setName("name1");
		entity.setSomeNum(1.1);
		entity.setOtherNum(11);
		entity.addItem(entity);
		
		entity.setId(2);
		entity.setName("name2");
		entity.setSomeNum(2.2);
		entity.setOtherNum(22);
		
		
		entity.addItem(entity);
		entity.getAll();


		Entity2 e = new Entity2(1,21,"name",1.5);


		Connection conn = ConnectionFactory.getConnection();



		e.addItem(e);




		
	}
}
