package com.bootcamp.bankapi.dao;

import com.bootcamp.bankapi.entity.Account;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@Repository
public class AccountDAO<T, ID extends Serializable> implements DAO<Account, String> {

    private final EntityManager entityManager;

    @Autowired
    public AccountDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Account get(String accountNumber) {
        Session session = entityManager.unwrap(Session.class);
        Query<Account> query = session.createQuery("from Account where number = :requestNumber", Account.class);
        query.setParameter("requestNumber", accountNumber);
        List<Account> accounts = query.getResultList();
        if (accounts.isEmpty()) {
            return null;
        }
        return accounts.get(0);
    }

    @Override
    public List<Account> getAll() {
        Session session = entityManager.unwrap(Session.class);
        List<Account> accounts;
        Query<Account> query = session.createQuery("from Account", Account.class);
        accounts = query.getResultList();
        return accounts;
    }

    @Override
    public void save(Account account) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(account);
    }

    @Override
    public void delete(Account account) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from Account where id = :accountId");
        query.setParameter("accountId", account.getId());
        query.executeUpdate();
    }
}
