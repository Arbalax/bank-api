package com.bootcamp.bankapi.dao;

import com.bootcamp.bankapi.entity.Operation;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@Repository
public class OperationDAO<T, ID extends Serializable> implements DAO<Operation, Integer> {

    private final EntityManager entityManager;

    @Autowired
    public OperationDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Operation get(Integer operationId) {
        Session session = entityManager.unwrap(Session.class);
        Operation operation = session.get(Operation.class, operationId);
        return operation;
//        Session session = entityManager.unwrap(Session.class);
//        Query<Operation> query = session.createQuery("from Operation where number = :requestId", Operation.class);
//        query.setParameter("requestId", operationId);
//        List<Operation> operations = query.getResultList();
//        if (operations.isEmpty()) {
//            return null;
//        }
//        return operations.get(0);
    }

    @Override
    public List<Operation> getAll() {
        Session session = entityManager.unwrap(Session.class);
        List<Operation> operations;
        Query<Operation> query = session.createQuery("from Operation", Operation.class);
        operations = query.getResultList();
        return operations;
    }

    @Override
    public void save(Operation operation) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(operation);
    }

    @Override
    public void delete(Operation operation) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from Operation where id = :operationId");
        query.setParameter("operationId", operation.getId());
        query.executeUpdate();
    }
}
