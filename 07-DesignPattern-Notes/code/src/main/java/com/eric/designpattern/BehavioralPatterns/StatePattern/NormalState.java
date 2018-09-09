package com.eric.designpattern.BehavioralPatterns.StatePattern;

import com.github.ectools.commons.results.UpdateResult;

/**
 * 正常状态
 * @author Chen 2018/9/9
 */
public class NormalState extends AccountState {

    public NormalState(Account account) {
        this.account = account;
    }

    public NormalState(AccountState accountState) {
        this.account = accountState.account;
    }

    @Override
    public String getStateName() {
        return "正常状态";
    }

    @Override
    public UpdateResult stateCheck() {


    }
}
