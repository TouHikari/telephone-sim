package com.touhikari.telephone.core.state;

import com.touhikari.telephone.core.CallController;
import com.touhikari.telephone.core.PhoneState;

public class PlayInfoState implements PhoneState {

    @Override
    public void onEnter(CallController ctx) {
        String number = ctx.getDialer().getNumber();
        if (ctx.getMessageStore().hasMessage(number)) {
            ctx.getAudio().playInfo(ctx.getMessageStore().getMessage(number));
            ctx.getTimer().reset();
            ctx.getTimer().start();
        } else {
            ctx.setState(new TimeoutBeepState());
        }
    }

    @Override
    public void onExit(CallController ctx) {
        ctx.getAudio().stopInfo();
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
        String number = ctx.getDialer().getNumber();
        if (ctx.getMessageStore().hasMessage(number)) {
            int duration = ctx.getMessageStore().getMessage(number).durationMs;
            if (ctx.getTimer().timeout(duration)) {
                ctx.setState(new DisconnectedState());
            }
        } else {
            ctx.setState(new DisconnectedState());
        }
    }

    @Override
    public void run() {
    }
}
