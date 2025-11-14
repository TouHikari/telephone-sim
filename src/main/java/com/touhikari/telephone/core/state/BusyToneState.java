package com.touhikari.telephone.core.state;

import com.touhikari.telephone.core.CallController;
import com.touhikari.telephone.core.PhoneState;

public class BusyToneState implements PhoneState {

    @Override
    public void onEnter(CallController ctx) {
        ctx.getAudio().playBusyTone();
    }

    @Override
    public void onExit(CallController ctx) {
        ctx.getAudio().stopBusyTone();
    }

    @Override
    public void onPickUp(CallController ctx) {
    }

    @Override
    public void onHangUp(CallController ctx) {
        ctx.setState(new DisconnectedState());
    }

    @Override
    public void onDigit(CallController ctx, char d) {
    }

    @Override
    public void onTimeOut(CallController ctx) {
        ctx.setState(new DisconnectedState());
    }

    @Override
    public void run() {
    }
}
