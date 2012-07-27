package com.test.server.beans;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class TestDAO {
	
	public TestDAO() {

    }

	
	@EJB
    TestDbManager dbManager;
	
	public void insertMessage(String message){
		
		TestEntity entity = new TestEntity(message);
        dbManager.create(entity);
    }
	
	public void deleteMessage(long id){
       	dbManager.deleteById(id);
	}
}
