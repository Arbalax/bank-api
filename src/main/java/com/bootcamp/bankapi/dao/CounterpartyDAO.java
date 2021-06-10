package com.bootcamp.bankapi.dao;

import com.bootcamp.bankapi.entity.Counterparty;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@Repository
public class CounterpartyDAO<T, ID extends Serializable> implements DAO<Counterparty, String> {

    private final EntityManager entityManager;

    @Autowired
    public CounterpartyDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Counterparty get(String counterpartyName) {

        Session session = entityManager.unwrap(Session.class);
        Query<Counterparty> query = session.createQuery("from Counterparty where name = :requestName", Counterparty.class);
        query.setParameter("requestNumber", counterpartyName);
        List<Counterparty> counterparties = query.getResultList();
        if (counterparties.isEmpty()) {
            return null;
        }
        return counterparties.get(0);
    }

    @Override
    public List<Counterparty> getAll() {

        Session session = entityManager.unwrap(Session.class);
        List<Counterparty> counterparties;
        Query<Counterparty> query = session.createQuery("from Counterparty", Counterparty.class);
        counterparties = query.getResultList();
        return counterparties;
    }

    @Override
    public void save(Counterparty counterparty) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(counterparty);
    }

    @Override
    public void delete(Counterparty counterparty) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from Counterparty where name = :counterpartyId");
        query.setParameter("counterpartyId", counterparty.getId());
        query.executeUpdate();
    }
}
