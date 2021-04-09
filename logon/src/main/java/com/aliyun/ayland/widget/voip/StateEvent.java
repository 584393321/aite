package com.aliyun.ayland.widget.voip;

import com.evideo.voip.sdk.EVVoipAccount;

/**
 * Created by lhr on 2020/1/7.
 */
public class StateEvent {

    private EVVoipAccount.AccountState state;

    public StateEvent(EVVoipAccount.AccountState state) {
        this.state = state;
    }

    public EVVoipAccount.AccountState getState() {
        return state;
    }

    public void setState(EVVoipAccount.AccountState state) {
        this.state = state;
    }
}
