package com.bootcamp.bankapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "passport")
    private String passport;

    @JsonManagedReference
    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.ALL})
    private List<Account> accounts;

    @JsonManagedReference
    @OneToMany(mappedBy = "user",
            cascade = {CascadeType.ALL})
    private List<Counterparty> counterparties;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Counterparty> getCounterparties() {
        return counterparties;
    }

    public void setCounterparties(List<Counterparty> counterparties) {
        this.counterparties = counterparties;
    }

    public void addAccount(Account account) {
        if (accounts == null) {
            accounts = new ArrayList<>();
        }
        accounts.add(account);
        account.setUser(this);
    }

    public void addCounterparty(Counterparty counterparty) {
        if (counterparties == null) {
            counterparties = new ArrayList<>();
        }
        counterparties.add(counterparty);
        counterparty.setUser(this);
    }
}
