package com.touhikari.telephone.core.state;

import com.touhikari.telephone.core.CallController;
import com.touhikari.telephone.core.PhoneState;

public class RingingState implements PhoneState {

    @Override
    public void onEnter(CallController ctx) {
    }

    @Override
    public void onExit(CallController ctx) {
    }

    @Override
    public void onPickUp(CallController ctx) {
    }

    @Override
    public void onHangUp(CallController ctx) {
    }

    @Override
    public void onDigit(CallController ctx, char d) {
    }

    @Override
    public void onTimeOut(CallController ctx) {
    }

    @Override
    public void run() {
    }
}
