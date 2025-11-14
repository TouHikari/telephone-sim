package com.touhikari.telephone.core;

import com.touhikari.telephone.core.state.BusyToneState;
import com.touhikari.telephone.core.state.DisconnectedState;
import com.touhikari.telephone.core.state.IdleState;
import com.touhikari.telephone.core.state.RingingState;
import com.touhikari.telephone.core.state.TalkingState;
import com.touhikari.telephone.network.Connection;
import com.touhikari.telephone.network.NetworkSwitch;
import com.touhikari.telephone.service.AudioService;
import com.touhikari.telephone.service.Dialer;
import com.touhikari.telephone.service.MessageStore;
import com.touhikari.telephone.service.Timer;

public class CallController {

    private PhoneState state;
    private final Timer timer;
    private final AudioService audio;
    private final Dialer dialer;
    private final NetworkSwitch networkSwitch;
    private final MessageStore messageStore;
    private Connection connection;

    private static final CallController INSTANCE = new CallController();

    private CallController() {
        this.state = new IdleState();
        this.timer = new Timer();
        this.audio = new AudioService();
        this.dialer = new Dialer();
        this.networkSwitch = new NetworkSwitch();
        this.messageStore = new MessageStore();
    }

    public static CallController getInstance() {
        return INSTANCE;
    }

    public void setState(PhoneState s) {
        this.state.onExit(this);
        this.state = s;
        this.state.onEnter(this);
    }

    public void onPickUp() {
        state.onPickUp(this);
    }

    public void onHangUp() {
        state.onHangUp(this);
    }

    public void onDigit(char d) {
        state.onDigit(this, d);
    }

    public void onTimeOut() {
        state.onTimeOut(this);
    }

    public void onConnected(Connection c) {
        this.connection = c;
        if (c == null) {
            setState(new DisconnectedState());
            return;
        }
        switch (c.status) {
            case BUSY ->
                setState(new BusyToneState());
            case CONNECTED ->
                setState(new TalkingState());
            case DISCONNECTED ->
                setState(new DisconnectedState());
            case TRYING ->
                setState(new RingingState());
        }
    }

    public void onBusy() {
        setState(new BusyToneState());
    }

    public void onAnswered() {
        setState(new TalkingState());
    }

    public void onRemoteHangUp() {
        setState(new DisconnectedState());
    }

    public void onInfoFinished() {
        setState(new DisconnectedState());
    }

    public Timer getTimer() {
        return timer;
    }

    public AudioService getAudio() {
        return audio;
    }

    public Dialer getDialer() {
        return dialer;
    }

    public NetworkSwitch getNetworkSwitch() {
        return networkSwitch;
    }

    public MessageStore getMessageStore() {
        return messageStore;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection c) {
        this.connection = c;
    }
}
