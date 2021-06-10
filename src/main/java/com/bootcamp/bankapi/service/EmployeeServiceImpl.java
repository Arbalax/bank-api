package com.bootcamp.bankapi.service;

import com.bootcamp.bankapi.dao.DAO;
import com.bootcamp.bankapi.dao.UserDAO;
import com.bootcamp.bankapi.entity.Account;
import com.bootcamp.bankapi.entity.Card;
import com.bootcamp.bankapi.entity.Operation;
import com.bootcamp.bankapi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    UserService userService;

    @Autowired
    @Qualifier("userDAO")
    private UserDAO<User, Integer> userDAO;

    @Autowired
    @Qualifier("accountDAO")
    private DAO<Account, String> accountDAO;

    @Autowired
    @Qualifier("cardDAO")
    private DAO<Card, String> cardDAO;

    @Autowired
    @Qualifier("operationDAO")
    private DAO<Operation, Integer> operationDAO;

    @Autowired
    public EmployeeServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = userDAO.getAll();
        return users;
    }

    @Override
    public User getUserByPassport(String passport) {
        User user = userDAO.getUserByPassport(passport);
        return user;
    }

    @Override
    public void addUser(User user) {
        userDAO.save(user);
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = accountDAO.getAll();
        return accounts;
    }

    @Override
    public void addAccount(Account account) {
        accountDAO.save(account);
    }

    @Override
    public List<Card> getCards() {
        List<Card> cards = cardDAO.getAll();
        return cards;
    }

    @Override
    public void confirmCard(Card card, String result) {
        card.setStatus(result);
        cardDAO.save(card);
    }

    @Override
    @Transactional
    public void synchronizeBalance() {
        List<Card> cards = this.getCards();
        for (Card card : cards) {
            if (card.getStatus().equalsIgnoreCase("close")) {
                card.setBalance(0);
                cardDAO.save(card);
            }
        }
        List<Account> accounts = this.getAccounts();
        for (Account account : accounts) {
            userService.refreshBalance(account);
            accountDAO.save(account);
        }
    }

    @Override
    public Operation getOperation(int operationId) {
        Operation operation = operationDAO.get(operationId);
        return operation;
    }

    @Override
    public List<Operation> getOperations() {
        List<Operation> operations = operationDAO.getAll();
        return operations;
    }

    @Override
    @Transactional
    public boolean confirmOperation(Operation operation) {
        Account account = operation.getAccount();
        double balance = account.getBalance();
        String action = operation.getAction();
        double amount = operation.getAmount();
        if (action.equals("-") && (balance - amount < 0)) {
            return false;
        }
        if (action.equals("+")) {
            balance += amount;
        }
        if (action.equals("-")) {
            balance -= amount;
        }
            account.setBalance(balance);
            accountDAO.save(account);
            userService.refreshBalance(account);
            operation.setStatus("confirmed");
            operationDAO.save(operation);
            return true;
    }
}
