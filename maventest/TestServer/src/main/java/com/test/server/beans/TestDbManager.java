package com.test.server.beans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class TestDbManager
 */
@Stateless
@LocalBean
public class TestDbManager {

    @PersistenceContext(unitName = "TestDbManager")
    private EntityManager manager;

    public TestEntity findById(Long id) {
        if (id == null)
            return null;
        return manager.find(TestEntity.class, id);
    }

    public void update(TestEntity newValue) {
        if (newValue == null)
            return;
        manager.merge(newValue);
    }

    public void deleteById(Long id) {
        if (id == null)
            return;
        TestEntity te = manager.find(TestEntity.class, id);
        if (te == null)
            return;
        manager.remove(te);
    }
    
    public void create(TestEntity entity)
    {
        if(entity==null)
            return;
        manager.persist(entity);
    }

}
