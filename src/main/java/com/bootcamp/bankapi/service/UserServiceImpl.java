package com.bootcamp.bankapi.service;

import com.bootcamp.bankapi.dao.DAO;
import com.bootcamp.bankapi.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("userDAO")
    private DAO<User, Integer> userDAO;

    @Autowired
    @Qualifier("accountDAO")
    private DAO<Account, String> accountDAO;

    @Autowired
    @Qualifier("cardDAO")
    private DAO<Card, String> cardDAO;

    @Autowired
    @Qualifier("counterpartyDAO")
    private DAO<Counterparty, String> counterpartyDAO;

    @Autowired
    @Qualifier("operationDAO")
    private DAO<Operation, Integer> operationDAO;

    @Override
    public User getUserById(int id) {
        return userDAO.get(id);
    }

    @Override
    public Account getAccountByNumber(String number) {
        return accountDAO.get(number);
    }

    @Override
    @Transactional
    public void refreshBalance(Account account) {
        double balance = account.getBalance();
        List<Card> cards = account.getCards();
        for (Card card : cards) {
            if (card.getStatus().equalsIgnoreCase("open")) {
                card.setBalance(balance);
                cardDAO.save(card);
            }
        }
    }

    @Override
    public List<Card> getCardsByAccountNumber(String accountNumber) {
        Account account = this.getAccountByNumber(accountNumber);
        if (account == null)
            return null;
        List<Card> cards = account.getCards();
        return cards;
    }

    @Override
    public void addCard(Account account, Card card) {
        account.addCard(card);
        cardDAO.save(card);
        accountDAO.save(account);
    }

    @Override
    public Card getCardByNumber(String cardNumber) {
        Card card = cardDAO.get(cardNumber);
        return card;
    }

    @Override
    public boolean deleteUnconfirmedCard(Card card) {
        if (card.getStatus().equalsIgnoreCase("unconfirmed")) {
            cardDAO.delete(card);
            return true;
        }
        return false;
    }

//    @Override
//    @Transactional
//    public void addFunds(Card card, double funds) {
//        card.setBalance(card.getBalance() + funds);
//        Account account = card.getAccount();
//        account.setBalance(account.getBalance() + funds);
//        cardDAO.save(card);
//        accountDAO.save(account);
//    }

    @Override
    @Transactional
    public Operation addFunds(Card card, double funds) {
        Account account = card.getAccount();
        Operation operation = new Operation(account, funds, "+");
        operationDAO.save(operation);
        return operation;
    }

    @Override
    public Counterparty getCounterpartyByName(User user, String name) {
        List <Counterparty> counterparties = user.getCounterparties();
        for (Counterparty counterparty : counterparties) {
            if (counterparty.getName().equalsIgnoreCase(name)) {
                return counterparty;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public boolean moneyTransaction(Account account, Counterparty counterparty, double amount) {
        double balance = account.getBalance();
        if (counterparty.takeMoney(amount)) {
            account.setBalance(balance - amount);
            accountDAO.save(account);
            this.refreshBalance(account);
            return true;
        }
        return false;
    }

    @Override
    public void addCounterparty(User user, Counterparty counterparty) {
        user.addCounterparty(counterparty);
        counterpartyDAO.save(counterparty);
        userDAO.save(user);
    }
}
