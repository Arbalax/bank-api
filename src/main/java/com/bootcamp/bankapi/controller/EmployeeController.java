package com.bootcamp.bankapi.controller;

import com.bootcamp.bankapi.entity.Account;
import com.bootcamp.bankapi.entity.Card;
import com.bootcamp.bankapi.entity.Operation;
import com.bootcamp.bankapi.entity.User;
import com.bootcamp.bankapi.service.EmployeeService;
import com.bootcamp.bankapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    Logger logger = Logger.getLogger(this.getClass().getName());

    EmployeeService employeeService;
    UserService userService;

    @Autowired
    public EmployeeController(EmployeeService employeeService,
                              UserService userService) {
        this.employeeService = employeeService;
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List <User> users = employeeService.getUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        logger.log(Level.INFO, "User to add = " + user);
        String passport = user.getPassport();
        User storedUser = employeeService.getUserByPassport(passport);
        if (storedUser != null)
            return new ResponseEntity<>(storedUser,HttpStatus.CONFLICT);
        employeeService.addUser(user);
        User newUser = employeeService.getUserByPassport(passport);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accounts = employeeService.getAccounts();
        if (accounts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        logger.log(Level.INFO, "Account to add = " + account);
        String requestAccountNumber = account.getNumber();
        Account storedAccount = userService.getAccountByNumber(requestAccountNumber);
        if (storedAccount != null) {
            return new ResponseEntity<>(storedAccount, HttpStatus.CONFLICT);
        }
        employeeService.addAccount(account);
        Account newAccount = userService.getAccountByNumber(requestAccountNumber);
        return new ResponseEntity<>(newAccount, HttpStatus.OK);
    }

    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getCards() {
        List<Card> cards = employeeService.getCards();
        if (cards.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @PutMapping("/cards/{cardNumber}")
    public ResponseEntity<Card> confirmCard(@RequestBody String result,
                                            @PathVariable String cardNumber) {
        logger.log(Level.INFO, "Card to confirm = " + cardNumber + " , result = " + result);
        Card card = userService.getCardByNumber(cardNumber);
        if (card == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        employeeService.confirmCard(card, result);
        Card newCard = userService.getCardByNumber(cardNumber);
        return new ResponseEntity<>(newCard, HttpStatus.OK);
    }

    @GetMapping("/operations")
    public ResponseEntity<List<Operation>> getOperations() {
        List<Operation> operations = employeeService.getOperations();
        if (operations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(operations, HttpStatus.OK);
    }

    @PutMapping("/operations/{operationId}")
    public ResponseEntity<Operation> confirmOperation(@RequestBody String result,
                                                      @PathVariable int operationId) {
        logger.log(Level.INFO, "Operation to confirm = " + operationId + " , result = " + result);
        Operation operation = employeeService.getOperation(operationId);
        if (operation == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (operation.getStatus().equalsIgnoreCase("confirmed")) {
            return new ResponseEntity<>(operation, HttpStatus.OK);
        }
        if (employeeService.confirmOperation(operation)) {
            Operation newOperation = employeeService.getOperation(operationId);
            return new ResponseEntity<>(newOperation, HttpStatus.OK);
        }
        return new ResponseEntity<>(operation, HttpStatus.CONFLICT);
    }
}
