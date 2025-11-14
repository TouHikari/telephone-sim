package com.touhikari.telephone.core.state;

import com.touhikari.telephone.core.CallController;
import com.touhikari.telephone.core.PhoneState;

public class DialingState implements PhoneState {

    @Override
    public void onEnter(CallController ctx) {
        ctx.getTimer().reset();
        ctx.getTimer().start();
    }

    @Override
    public void onExit(CallController ctx) {
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
        ctx.getDialer().addDigit(d);
        ctx.getTimer().reset();
        ctx.getTimer().start();
    }

    @Override
    public void onTimeOut(CallController ctx) {
        if (!ctx.getTimer().timeout(3000)) {
            return;
        }
        String number = ctx.getDialer().getNumber();
        boolean valid = ctx.getDialer().isValid(number);
        if (valid) {
            ctx.setState(new ConnectingState());
            return;
        }
        if (ctx.getMessageStore().hasMessage(number)) {
            ctx.setState(new PlayInfoState());
            return;
        }
        ctx.setState(new TimeoutBeepState());
    }

    @Override
    public void run() {
    }
}
