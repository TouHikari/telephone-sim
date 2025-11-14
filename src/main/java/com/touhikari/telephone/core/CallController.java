package com.touhikari.telephone.core;

import com.touhikari.telephone.core.state.IdleState;
import com.touhikari.telephone.network.Connection;
import com.touhikari.telephone.service.Timer;

public class CallController {

    private PhoneState state;
    private String numberBuffer;
    private Timer timer;

    private static final CallController INSTANCE = new CallController();

    private CallController() {
        this.state = new IdleState();
    }

    public static CallController getInstance() {
        return INSTANCE;
    }

    public void setState(PhoneState s) {
        this.state = s;
    }

    public void onPickUp() {

    }

    public void onHangUp() {

    }

    public void onDigit(char d) {

    }

    public void onTimeOut() {

    }

    public void onConnected(Connection c) {

    }

    public void onBusy() {

    }

    public void onAnswered() {

    }

    public void onRemoteHangUp() {

    }

    public void onInfoFinished() {

    }
}
