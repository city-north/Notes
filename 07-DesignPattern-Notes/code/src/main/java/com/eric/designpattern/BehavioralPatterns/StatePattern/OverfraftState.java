package com.eric.designpattern.BehavioralPatterns.StatePattern;

/**
 * 透支状态
 * @author Chen 2018/9/9
 */
public class OverfraftState extends AccountState {

    public OverfraftState(Account account) {
        this.account = account;
    }

    public OverfraftState(AccountState accountState) {
        this.account = accountState.account;
    }

    @Override
    public String getStateName() {
        return "正常状态";
    }

    @Override
    public void deposit(double amount) {

    }

    @Override
    public void withdraw(double amount) {

    }


    @Override
    public UpdateResult stateCheck() {
//        if (account)
    }
}
