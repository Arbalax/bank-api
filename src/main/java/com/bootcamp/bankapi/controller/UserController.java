package com.bootcamp.bankapi.controller;

import com.bootcamp.bankapi.entity.*;
import com.bootcamp.bankapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger logger = Logger.getLogger(this.getClass().getName());

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser (@PathVariable int userId) {
        logger.log(Level.INFO, "Id = " + userId);

        User user = userService.getUserById(userId);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}/accounts/{number}")
    public ResponseEntity<Account> getAccount (@PathVariable String number) {
        logger.log(Level.INFO, "Account Number = " + number);

        Account account = userService.getAccountByNumber(number);
        if (account == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping("/{userId}/accounts/{accountNumber}/cards")
    public ResponseEntity<List<Card>> getCardsByAccountNumber(@PathVariable String accountNumber) {
        logger.log(Level.INFO, "Account number = " + accountNumber);
        List<Card> cards = userService.getCardsByAccountNumber(accountNumber);
        if (cards == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @PostMapping("/{userId}/accounts/{accountNumber}/cards")
    public ResponseEntity<Card> addCardByAccountNumber(@RequestBody Card card, @PathVariable String accountNumber) {
        logger.log(Level.INFO, "Card to add = " + card);
        String requestCardNumber = card.getNumber();
        Card storedCard = userService.getCardByNumber(requestCardNumber);
        if (storedCard != null)
            return new ResponseEntity<>(storedCard,HttpStatus.CONFLICT);
        Account account = userService.getAccountByNumber(accountNumber);
        if (account == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        account.addCard(card);
        userService.addCard(account, card);
        Card newCard = userService.getCardByNumber(requestCardNumber);
        return new ResponseEntity<>(newCard, HttpStatus.OK);
    }

    @PutMapping("/{userId}/accounts/{accountNumber}/cards/{cardNumber}")
    public ResponseEntity<Operation> addFunds(@RequestBody double funds, @PathVariable String cardNumber) {
        logger.log(Level.INFO, "Funds to add = " + funds);
        Card card = userService.getCardByNumber(cardNumber);
        if (card == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Operation operation = userService.addFunds(card, funds);
//        double balance = userService.getCardByNumber(card.getNumber()).getBalance();
        return new ResponseEntity<>(operation, HttpStatus.OK);

    }

    @DeleteMapping("/{userId}/accounts/{accountNumber}/cards/{cardNumber}")
    public ResponseEntity<Card> deleteUnconfirmedCard(@PathVariable String cardNumber) {
        logger.log(Level.INFO, "Card to delete = " + cardNumber);
        Card card = userService.getCardByNumber(cardNumber);
        if (card == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        boolean result = userService.deleteUnconfirmedCard(card);
        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(card, HttpStatus.CONFLICT);
    }


    @GetMapping("/{userId}/accounts/{accountNumber}/cards/{cardNumber}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable String cardNumber) {
        logger.log(Level.INFO, "Card number = " + cardNumber);
        Card card = userService.getCardByNumber(cardNumber);
        if (card == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        double balance = card.getBalance();
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @GetMapping("/{userId}/counterparties")
    public ResponseEntity<List<Counterparty>> getCounterparties (@PathVariable int userId) {
        logger.log(Level.INFO, "User Id = " + userId);
        User user = userService.getUserById(userId);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<Counterparty> counterparties = user.getCounterparties();
        if (counterparties.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(counterparties, HttpStatus.OK);
    }

    @PostMapping("/{userId}/counterparties")
    public ResponseEntity<Counterparty> addCounterparty(@RequestBody Counterparty counterparty, @PathVariable int userId) {
        logger.log(Level.INFO, "Counterparty to add = " + userId);
        User user = userService.getUserById(userId);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        String requestCounterpartyName = counterparty.getName();
        Counterparty storedCounterparty = userService.getCounterpartyByName(user, requestCounterpartyName);
        if (storedCounterparty != null) {
            return new ResponseEntity<>(storedCounterparty, HttpStatus.CONFLICT);
        }
        userService.addCounterparty(user, counterparty);
//        user.addCounterparty(counterparty);
        User newUser = userService.getUserById(userId);
        Counterparty newCounterparty = userService.getCounterpartyByName(newUser, requestCounterpartyName);
        return new ResponseEntity<>(newCounterparty, HttpStatus.OK);
    }

    @PutMapping("/{userId}/counterparties/{counterpartyName}/accounts/{accountNumber}")
    public ResponseEntity<Double> transferMoneyToCounterparty(@RequestBody double amount, @PathVariable int userId, @PathVariable String counterpartyName,
                                                              @PathVariable String accountNumber) {
        logger.log(Level.INFO, "Transfer " + amount + " to " + counterpartyName);
        User user = userService.getUserById(userId);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Counterparty counterparty = userService.getCounterpartyByName(user, counterpartyName);
        Account account = userService.getAccountByNumber(accountNumber);
        if (counterparty == null || account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        double balance = account.getBalance();
        if (balance < amount) {
            return new ResponseEntity<>(balance, HttpStatus.CONFLICT);
        }
        if (userService.moneyTransaction(account, counterparty, amount)) {
            double newBalance = account.getBalance();
            return new ResponseEntity<>(newBalance, HttpStatus.OK);
        } else {
            double newBalance = account.getBalance();
            return new ResponseEntity<>(newBalance, HttpStatus.SERVICE_UNAVAILABLE);
        }

    }
}
