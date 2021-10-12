package com.sapheworkshop.atminterface;

import java.util.Scanner;

public class ATM {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Bank theBank = new Bank("Bank of Scandinavia");

        User aUser = theBank.addUser("Sara", "Milch", "1234");

        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true){

            curUser = ATM.mainMenuPrompt(theBank, sc);

            ATM.printUserMenu(curUser, sc);

        }

    }

    private static void printUserMenu(User theUser, Scanner sc) {

        theUser.printAccountSummary();

        int choice;

        do {
            System.out.printf("Welcome %s, what would you like to do?\n", theUser.getFirstName());
            System.out.println(" 1) Show transaction history");
            System.out.println(" 2) Withdraw");
            System.out.println(" 3) Deposit");
            System.out.println(" 4) Transfer");
            System.out.println(" 5) Quit");
            System.out.println();
            do{
                System.out.println("Please enter the choice: ");
                while(!sc.hasNextInt()){
                    System.out.println("Invalid choice. Please choose 1-5");
                }
                choice = sc.nextInt();
            }while(choice < 0);

            if(choice < 1 || choice > 5){
                System.out.println("Invalid choice. Please choose 1-5");
            }
        }while(choice < 1 || choice > 5);

        switch (choice){
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
            case 5:
                sc.nextLine();
                break;

        }
        if(choice != 5) {
            ATM.printUserMenu(theUser, sc);
        }

    }

    private static void depositFunds(User theUser, Scanner sc) {
        int toAccount;
        double amount;
        String memo;

        do{
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to deposit to: ", theUser.numAccounts());
            toAccount = sc.nextInt()-1;
            if(toAccount < 0 || toAccount >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        }while(toAccount < 0 || toAccount >= theUser.numAccounts());

        do{
            System.out.println("Enter the amount to deposit : $");
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            }
        } while (amount < 0);
        sc.nextLine();
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();
        theUser.addAccountTransaction(toAccount, amount, memo);
    }

    private static void withdrawFunds(User theUser, Scanner sc) {
        int fromAccount;
        double amount;
        double accBalance;
        String memo;
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to withdraw from: ", theUser.numAccounts());
            fromAccount = sc.nextInt() - 1;
            if (fromAccount < 0 || fromAccount >= theUser.numAccounts()) {
                System.out.println("Invalid account. Please try again");
            }
        } while (fromAccount < 0 || fromAccount >= theUser.numAccounts());
        accBalance = theUser.getAccountBalance(fromAccount);

        do {
            System.out.printf("Enter the amount to withdraw (max $%.02f): $",
                    accBalance);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > accBalance) {
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $%.02f\n", accBalance);
            }
        } while (amount < 0 || amount > accBalance);
        sc.nextLine();
        System.out.println("Enter a memo: ");
        memo = sc.nextLine();
        theUser.addAccountTransaction(fromAccount, -1*amount, memo);

    }

    private static void transferFunds(User theUser, Scanner sc) {
        int fromAccount;
        int toAccount;
        double amount;
        double accBalance;

        do{
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer from: ", theUser.numAccounts());
            fromAccount = sc.nextInt()-1;
            if(fromAccount < 0 || fromAccount >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        }while(fromAccount < 0 || fromAccount >= theUser.numAccounts());
        accBalance = theUser.getAccountBalance(fromAccount);

        do{
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer to: ", theUser.numAccounts());
            toAccount = sc.nextInt()-1;
            if(toAccount < 0 || toAccount >= theUser.numAccounts()){
                System.out.println("Invalid account. Please try again");
            }
        }while(toAccount < 0 || toAccount >= theUser.numAccounts());

        do{
            System.out.printf("Enter the amount to transfer (max $%.02f): $",
            accBalance);
            amount = sc.nextDouble();
            if(amount < 0){
                System.out.println("Amount must be greater than 0.");
            }else if(amount  > accBalance){
                System.out.printf("Amount must not be greater than\n" +
                        "balance of $%.02f\n", accBalance);
            }
        }while(amount < 0 || amount > accBalance);

        theUser.addAccountTransaction(fromAccount, -1*amount, String.format(
                "Transfer to account %s", theUser.getAccountUUID(toAccount)));
        theUser.addAccountTransaction(toAccount, amount, String.format(
                "Transfer to account %s", theUser.getAccountUUID(fromAccount)));


    }

    private static void showTransHistory(User theUser, Scanner sc) {

        int theAccount;
        do{
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    " whose transactions you want to see:",
                    theUser.numAccounts());
            theAccount = sc.nextInt()-1;
            if(theAccount < 0 || theAccount >= theUser.numAccounts()){
                System.out.println("Invalid account.");
            }

        }while(theAccount < 0 || theAccount >= theUser.numAccounts());

        theUser.printAcctTransHistory(theAccount);

    }

    private static User mainMenuPrompt(Bank theBank, Scanner sc) {

        String userID;
        String pin;
        User authUser;

        do{
            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter user ID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();

            authUser = theBank.userLogin(userID, pin);
            if(authUser == null){
                System.out.println("Incorrect user ID/pin combination. " + "Please try again.");
            }
        }while(authUser == null);

        return authUser;
    }
}
