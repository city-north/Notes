package com.eric.designpattern.BehavioralPatterns.StatePattern;

import com.github.ectools.commons.results.UpdateResult;

/**
 * 受限状态
 * @author Chen 2018/9/9
 */
public class RestrictedState extends AccountState {

    public RestrictedState(Account account) {
        this.account = account;
    }

    public RestrictedState(AccountState accountState) {
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
