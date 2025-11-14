package com.touhikari.telephone.network;

public class Endpoint {

    private boolean ringing;
    private boolean answered;
    private boolean hungUp;

    public void ring() {
        if (ringing) {
            return;
        }
        ringing = true;
        answered = false;
        hungUp = false;
    }

    public void answer() {
        if (answered) {
            return;
        }
        answered = true;
        ringing = false;
    }

    public void hangUp() {
        if (hungUp) {
            return;
        }
        hungUp = true;
        ringing = false;
        answered = false;
    }

    public boolean isRinging() {
        return ringing;
    }

    public boolean isAnswered() {
        return answered;
    }

    public boolean isHungUp() {
        return hungUp;
    }
}
