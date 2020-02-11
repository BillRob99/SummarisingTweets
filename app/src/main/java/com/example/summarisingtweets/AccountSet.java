package com.example.summarisingtweets;

import java.util.ArrayList;
import java.io.Serializable;
import twitter4j.User;

/**
 * This class represents a set of Twitter accounts that the user has created.
 *
 * @author William Roberts
 * @version 1.0
 */
public class AccountSet implements Serializable{
    private String name;
    private int noOfAccounts;
    private ArrayList<User> accountList = new ArrayList();

    /**
     * The constructor for the class.
     * @param name The name of the set.
     */
    public AccountSet(String name) {
        this.name = name;
        this.noOfAccounts = 0;
    }

    /**
     * This method is used to set the name of the set.
     * @param name The name of the set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the name of the set.
     * @return The name of the set.
     */
    public String getName() {
        return name;
    }

    /**
     * This method returns the number of accounts in the set.
     * @return The number of accounts in the set.
     */
    public int getNoOfAccounts() {
        return noOfAccounts;
    }

    /**
     * Convenience method for returning the complete list of accounts.
     * @return ArrayList of accounts in the AccountSet.
     */
    public ArrayList<User> getAccountList() {
        return accountList;
    }

    /**
     * This method adds an account to the set of accounts. Since sets are ordered
     * alphabetically by their screen names, it inserts the account into the correct index.
     *
     * @param account The Twitter account to be added.
     * @return true if the account has been added, false if the account already exists in the list.
     */
    public boolean addAccount(User account) {

        if(accountList.size() == 0) {
            accountList.add(account);
            noOfAccounts ++;
            return true;
        }

        for(int i = 0; i < accountList.size(); i++) {
            //This value will be positive if the account being added's display name
            // is alphabetically less than that of the current index.
            int compValue = accountList.get(i).getScreenName().compareTo(account.getScreenName());

            //If the values are the same.
            if(compValue == 0) {
                return false;

            } else if(compValue > 0) {
                //Add the account to the correct index in the list.
                accountList.add(i, account);
                noOfAccounts ++;
                return true;
            }
        }
        //This will be called if the item needs to be added to the end of the list.
        accountList.add(account);
        noOfAccounts ++;
        return true;
    }

    /**
     * This method removes an account from the set.
     *
     * @param account The account to be removed.
     */
    public void removeAccount(User account) {
        for(int i = 0; i < accountList.size(); i++) {
            if(accountList.get(i).getId() == account.getId()) {
                accountList.remove(i);
                noOfAccounts --;
            }
        }
    }

    /**
     * This method checks whether an account is currently in the set.
     * @param account The account that is being checked for.
     * @return Boolean value of true if is is in the set, false otherwise.
     */
    public boolean checkIfContains(User account) {
        for(int i = 0; i < accountList.size(); i++) {
            if(accountList.get(i).getId() == account.getId()) {
                return true;
            }
        }
        return false;
    }

}