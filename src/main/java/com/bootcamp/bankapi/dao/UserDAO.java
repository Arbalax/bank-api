package com.bootcamp.bankapi.dao;

import com.bootcamp.bankapi.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@Repository
public class UserDAO<T, ID extends Serializable> implements DAO<User, Integer> {

    private final EntityManager entityManager;

    @Autowired
    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User get(Integer userId) {
        Session session = entityManager.unwrap(Session.class);
        User user = session.get(User.class, userId);
        return user;
    }

    public User getUserByPassport(String passport) {
        Session session = entityManager.unwrap(Session.class);
        Query<User> query = session.createQuery("from User where passport = :requestPassport", User.class);
        query.setParameter("requestPassport", passport);
        List<User> users = query.getResultList();
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public List<User> getAll() {
        Session session = entityManager.unwrap(Session.class);
        List<User> users;
        Query<User> query = session.createQuery("from User", User.class);
        users = query.getResultList();
        return users;
    }

    @Override
    public void save(User user) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(user);
    }

    @Override
    public void delete(User user) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from User where id = :userId");
        query.setParameter("userId", user.getId());
        query.executeUpdate();
    }
}
