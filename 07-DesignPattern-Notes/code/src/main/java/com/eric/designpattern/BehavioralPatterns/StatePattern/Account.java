package com.eric.designpattern.BehavioralPatterns.StatePattern;

import com.github.ectools.commons.results.UpdateResult;

/**
 * 银行账户类
 * @author Chen 2018/9/9
 */
public class Account {
    private AccountState accountState;
    /**
     * 拥有者
     */
    private String owner;
    private double balance = 0;

    private Account(String owner, double balance) {
        this.owner = owner;
        this.balance = balance;
        System.out.println(owner +"开户，初始余额为"+balance);
    }

    private String getOwner() {
        return owner;
    }

    private double getBalance() {
        return balance;
    }

    public static class AccountManager {
        private Account account;

        private AccountManager (){}
        public AccountManager createAccount(String owner, double balance) {
            account = new Account(owner, balance);
            return this;
        }

        /**
         * 存款
         */
        public UpdateResult deposit(double amount) {
            System.out.println("[" + account.getOwner() + "]账户正在进行存款操作，存款金额为" + amount);
            UpdateResult result = account.accountState.stateCheck();
            if (result.isSuccess()) {
                account.balance = account.balance + amount;
            }
            return result;
        }

        /**
         * 取款
         *
         * @param amount
         */
        public UpdateResult withdraw(double amount) {
            System.out.println("[" + account.owner + "]账户正在进行取款操作，存款金额为" + amount);
            UpdateResult result = account.accountState.stateCheck();
            if (result.isSuccess()) {
                account.balance = account.balance - amount;
            }
            return result;
        }

        public String getAccountState() {
            return account.accountState.getStateName();
        }

        public String getOwner() {
            return account.getOwner();
        }
    }
}
