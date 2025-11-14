package com.touhikari.telephone.core.state;

import com.touhikari.telephone.core.CallController;
import com.touhikari.telephone.core.PhoneState;

public class TimeoutBeepState implements PhoneState {

    @Override
    public void onEnter(CallController ctx) {
        ctx.getAudio().playBeep();
        ctx.getTimer().reset();
        ctx.getTimer().start();
    }

    @Override
    public void onExit(CallController ctx) {
        ctx.getAudio().stopBeep();
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
        ctx.setState(new DialingState());
        ctx.onDigit(d);
    }

    @Override
    public void onTimeOut(CallController ctx) {
        ctx.setState(new IdleState());
    }

    @Override
    public void run() {
    }
}
