package com.bootcamp.bankapi.service;

import com.bootcamp.bankapi.entity.Account;
import com.bootcamp.bankapi.entity.Card;
import com.bootcamp.bankapi.entity.Operation;
import com.bootcamp.bankapi.entity.User;

import java.util.List;

public interface EmployeeService {

    public List<User> getUsers();

//    public User getUserById(int id);

    public User getUserByPassport(String passport);

    public void addUser(User user);

    List<Account> getAccounts();

    public void addAccount(Account account);

    List<Card> getCards();

    public void confirmCard(Card card, String result);

    public void synchronizeBalance();

    public boolean confirmOperation(Operation operation);

    public Operation getOperation(int operationId);


    List<Operation> getOperations();
}
