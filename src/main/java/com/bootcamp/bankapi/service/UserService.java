package com.bootcamp.bankapi.service;

import com.bootcamp.bankapi.entity.*;

import java.util.List;

public interface UserService {

    public User getUserById(int id);

    public Account getAccountByNumber(String number);

    public void refreshBalance(Account account);

    public List<Card> getCardsByAccountNumber(String accountNumber);

    public void addCard(Account account, Card card);

    public Card getCardByNumber(String cardNumber);

    public boolean deleteUnconfirmedCard(Card card);

    public Operation addFunds(Card card, double funds);

    public Counterparty getCounterpartyByName(User user, String name);

    public boolean moneyTransaction(Account account, Counterparty counterparty, double amount);

    public void addCounterparty(User user, Counterparty counterparty);
}
