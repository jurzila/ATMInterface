package com.sapheworkshop.atminterface;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {

    private String firstName;
    private String lastName;
    private String uuid;
    private byte pinHash[];
    private ArrayList<Account> accounts;

    public User(String firstName, String lastName, String pin, Bank theBank){
        this.firstName = firstName;
        this.lastName = lastName;

        // store the pin's MD5 hash, rather than the original value, for security reasons
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("error, caught NoSuchAlgorithmException");
            ex.printStackTrace();
            System.exit(1);
        }

        // get a new, unique universal ID for the user
        this.uuid = theBank.getNewUserUUID();

        // create an empty list of accounts
        this.accounts = new ArrayList<Account>();

        // print log message
        System.out.printf("New user %s, %s with ID %s created.\n", lastName, firstName, uuid);

    }


    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public String getUUID() {
        return this.uuid;
    }

    public boolean validatePin(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(pin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void printAccountSummary() {

        System.out.printf("\n\n%s's accounts summary\n", this.firstName);
        for (int i = 0; i < this.accounts.size(); i++){
            System.out.printf("%d) %s\n", i+1, this.accounts.get(i).getSummaryLine());
        }
        System.out.println();


    }

    public int numAccounts() {
        return this.accounts.size();
    }

    public void printAcctTransHistory(int theAccountIndex) {
        this.accounts.get(theAccountIndex).printTransHistory();


    }

    public double getAccountBalance(int accIndex) {
        return this.accounts.get(accIndex).getBalance();
    }

    public String getAccountUUID(int accIndex) {
        return this.accounts.get(accIndex).getUUID();
    }

    public void addAccountTransaction(int accIndex, double amount, String memo) {
        this.accounts.get(accIndex).addTransaction(amount, memo);
    }
}
