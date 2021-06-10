package com.bootcamp.bankapi.dao;

import com.bootcamp.bankapi.entity.Card;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@Repository
public class CardDAO<T, ID extends Serializable> implements DAO<Card, String> {

    private final EntityManager entityManager;

    @Autowired
    public CardDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Card get(String cardNumber) {
        Session session = entityManager.unwrap(Session.class);
        Query<Card> query = session.createQuery("from Card where number = :requestNumber", Card.class);
        query.setParameter("requestNumber", cardNumber);
        List<Card> cards = query.getResultList();
        if (cards.isEmpty()) {
            return null;
        }
        return cards.get(0);
    }

    @Override
    public List<Card> getAll() {
        Session session = entityManager.unwrap(Session.class);
        List<Card> cards;
        Query<Card> query = session.createQuery("from Card", Card.class);
        cards = query.getResultList();
        return cards;
    }

    @Override
    public void save(Card card) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(card);
    }

    @Override
    public void delete(Card card) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from Card where id = :cardId");
        query.setParameter("cardId", card.getId());
        query.executeUpdate();
    }
}
