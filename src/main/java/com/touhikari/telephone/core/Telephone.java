package com.touhikari.telephone.core;

import com.touhikari.telephone.network.NetworkSwitch;
import com.touhikari.telephone.service.AudioService;
import com.touhikari.telephone.service.Dialer;
import com.touhikari.telephone.service.MessageStore;

public class Telephone {

    private Handset handset;
    private static CallController controller;
    private AudioService audio;
    private Dialer dialer;
    private NetworkSwitch networkSwitch;
    private MessageStore messageStore;

    public void pickUp() {
        if (controller == null) {
            controller = CallController.getInstance();
        }
        if (handset == null) {
            handset = new Handset();
        }
        handset.lift();
    }

    public void hangUp() {
        if (handset == null) {
            handset = new Handset();
        }
        handset.replace();
    }

    public void inputDigit(char d) {
        if (controller == null) {
            controller = CallController.getInstance();
        }
        controller.onDigit(d);
    }

    public static void main(String[] args) {
        controller = CallController.getInstance();
    }
}
