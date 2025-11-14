package com.touhikari.telephone.core;

public interface PhoneState {

    public void onEnter(CallController ctx);

    public void onExit(CallController ctx);

    public void onPickUp(CallController ctx);

    public void onHangUp(CallController ctx);

    public void onDigit(CallController ctx, char d);

    public void onTimeOut(CallController ctx);

    public void run();
}
