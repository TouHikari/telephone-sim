package com.touhikari.telephone.core.state;

import com.touhikari.telephone.core.CallController;
import com.touhikari.telephone.core.PhoneState;

public class DisconnectedState implements PhoneState {

    @Override
    public void onEnter(CallController ctx) {
        ctx.getAudio().stopVoice();
        ctx.getAudio().stopRinging();
        ctx.getAudio().stopDialTone();
        ctx.getAudio().stopBusyTone();
        ctx.getAudio().stopBeep();
        ctx.getAudio().stopInfo();
        ctx.getDialer().clear();
        ctx.getTimer().reset();
        ctx.setConnection(null);
        ctx.setState(new IdleState());
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
