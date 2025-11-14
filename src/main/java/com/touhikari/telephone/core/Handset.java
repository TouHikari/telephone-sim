package com.touhikari.telephone.core;

public class Handset {

    public boolean isOffHook;

    public void lift() {
        if (isOffHook) {
            return;
        }
        isOffHook = true;
        CallController.getInstance().onPickUp();
    }

    public void replace() {
        if (!isOffHook) {
            return;
        }
        isOffHook = false;
        CallController.getInstance().onHangUp();
    }
}
