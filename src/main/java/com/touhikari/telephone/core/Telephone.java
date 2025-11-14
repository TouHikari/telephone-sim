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

    }

    public void hangUp() {

    }

    public void inputDigit(char d) {

    }

    public static void main(String[] args) {
        controller = CallController.getInstance();
    }
}
