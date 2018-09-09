package com.eric.designpattern.BehavioralPatterns.StatePattern;

import com.github.ectools.commons.results.UpdateResult;

/**
 * @author Chen 2018/9/9
 */
public abstract class AccountState {
    Account.AccountManager accountManager =null;


    public abstract String getStateName();

    /**
     * 查询账户状态
     */
    public abstract UpdateResult stateCheck();
}
