package com.touhikari.telephone.core.state;

import com.touhikari.telephone.core.CallController;
import com.touhikari.telephone.core.PhoneState;

public class RingingState implements PhoneState {

    @Override
    public void onEnter(CallController ctx) {
        ctx.getAudio().startRinging();
        ctx.getTimer().reset();
        ctx.getTimer().start();
    }

    @Override
    public void onExit(CallController ctx) {
        ctx.getAudio().stopRinging();
    }

    @Override
    public void onPickUp(CallController ctx) {
    }

    @Override
    public void onHangUp(CallController ctx) {
        if (ctx.getConnection() != null) {
            ctx.getConnection().disconnect();
        }
        ctx.setState(new DisconnectedState());
    }

    @Override
    public void onDigit(CallController ctx, char d) {
    }

    @Override
    public void onTimeOut(CallController ctx) {
        if (ctx.getTimer().timeout(3000)) {
            ctx.setState(new TalkingState());
        }
    }

    @Override
    public void run() {
    }
}
