package com.touhikari.telephone.core.state;

import com.touhikari.telephone.core.CallController;
import com.touhikari.telephone.core.PhoneState;
import com.touhikari.telephone.network.Connection;
import com.touhikari.telephone.network.ConnectionStatus;

public class ConnectingState implements PhoneState {

    @Override
    public void onEnter(CallController ctx) {
        Connection c = ctx.getNetworkSwitch().connect(ctx.getDialer().getNumber());
        ctx.setConnection(c);
        if (c.status == ConnectionStatus.BUSY) {
            ctx.setState(new BusyToneState());
            return;
        }
        if (c.status == ConnectionStatus.CONNECTED) {
            ctx.setState(new TalkingState());
            return;
        }
        ctx.setState(new RingingState());
    }

    @Override
    public void onExit(CallController ctx) {
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
    }

    @Override
    public void run() {
    }
}
