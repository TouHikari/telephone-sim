package com.touhikari.telephone.core;

import com.touhikari.telephone.network.NetworkSwitch;
import com.touhikari.telephone.service.AudioService;
import com.touhikari.telephone.service.Dialer;
import com.touhikari.telephone.service.MessageStore;
import java.util.Scanner;

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
        Telephone app = new Telephone();
        try {
            System.setOut(new java.io.PrintStream(new java.io.BufferedOutputStream(System.out), true, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
        }
        Scanner sc = new Scanner(System.in, "UTF-8");
        System.out.println("Telephone Simulator");
        System.out.println("Type 'help' for commands");
        long lastTick = System.nanoTime();
        boolean running = true;
        while (running) {
            if (System.nanoTime() - lastTick >= 200_000_000L) {
                lastTick = System.nanoTime();
                String state = controller.getStateName();
                if ("DialToneState".equals(state) && controller.getAudio().isDialTonePlaying() && controller.getTimer().timeout(5000)) {
                    controller.onTimeOut();
                }
                if ("TimeoutBeepState".equals(state) && controller.getAudio().isBeepPlaying() && controller.getTimer().timeout(2000)) {
                    controller.onTimeOut();
                }
                if ("DialingState".equals(state) && controller.getTimer().timeout(3000)) {
                    controller.onTimeOut();
                }
                if ("RingingState".equals(state) && controller.getAudio().isRinging() && controller.getTimer().timeout(3000)) {
                    controller.onTimeOut();
                }
                if ("BusyToneState".equals(state) && controller.getAudio().isBusyTonePlaying() && controller.getTimer().timeout(3000)) {
                    controller.onTimeOut();
                }
                if ("PlayInfoState".equals(state) && controller.getAudio().isInfoPlaying()) {
                    controller.onTimeOut();
                }
            }
            System.out.print("> ");
            if (!sc.hasNextLine()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    running = false;
                }
                continue;
            }
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0].toLowerCase();
            if ("help".equals(cmd)) {
                System.out.println("Commands: pickup, hangup, digit <0-9>, call <number>, status, numbers, answer, remotehangup, timeout, exit");
                continue;
            }
            if ("pickup".equals(cmd)) {
                app.pickUp();
                System.out.println("Picked up");
                continue;
            }
            if ("hangup".equals(cmd)) {
                app.hangUp();
                System.out.println("Hung up");
                continue;
            }
            if ("digit".equals(cmd)) {
                if (parts.length < 2 || parts[1].length() == 0) {
                    System.out.println("Digit required");
                    continue;
                }
                char d = parts[1].charAt(0);
                app.inputDigit(d);
                System.out.println("Digit " + d + " entered");
                continue;
            }
            if ("call".equals(cmd)) {
                if (parts.length < 2 || parts[1].isEmpty()) {
                    System.out.println("Number required");
                    continue;
                }
                String num = parts[1].trim();
                app.pickUp();
                for (int i = 0; i < num.length(); i++) {
                    app.inputDigit(num.charAt(i));
                }
                System.out.println("Number entered: " + num);
                continue;
            }
            if ("status".equals(cmd)) {
                String state = controller.getStateName();
                String number = controller.getDialer().getNumber();
                String conn = controller.getConnection() == null ? "none" : controller.getConnection().status.name();
                System.out.println("State: " + state);
                System.out.println("DialTone: " + controller.getAudio().isDialTonePlaying());
                System.out.println("BusyTone: " + controller.getAudio().isBusyTonePlaying());
                System.out.println("Beep: " + controller.getAudio().isBeepPlaying());
                System.out.println("Ringing: " + controller.getAudio().isRinging());
                System.out.println("Voice: " + controller.getAudio().isVoiceActive());
                System.out.println("Info: " + controller.getAudio().isInfoPlaying());
                if (controller.getAudio().isInfoPlaying() && controller.getAudio().getCurrentMessage() != null) {
                    System.out.println("Message: " + controller.getAudio().getCurrentMessage().text + " (" + controller.getAudio().getCurrentMessage().durationMs + " ms)");
                }
                System.out.println("Number: " + number);
                System.out.println("Connection: " + conn);
                continue;
            }
            if ("numbers".equals(cmd)) {
                System.out.println("Built-in numbers:");
                System.out.println("111 -> immediate connect");
                System.out.println("222 -> rings then connects after 3s");
                System.out.println("555 -> busy");
                System.out.println("404 -> info: not exist");
                System.out.println("500 -> info: busy later");
                System.out.println("999 -> info: service not available");
                System.out.println("888xxx -> busy");
                System.out.println("*00 -> busy");
                continue;
            }
            if ("answer".equals(cmd)) {
                controller.onAnswered();
                System.out.println("Remote answered");
                continue;
            }
            if ("remotehangup".equals(cmd)) {
                controller.onRemoteHangUp();
                System.out.println("Remote hung up");
                continue;
            }
            if ("timeout".equals(cmd)) {
                controller.onTimeOut();
                System.out.println("Timeout signaled");
                continue;
            }
            if ("exit".equals(cmd)) {
                running = false;
                System.out.println("Bye");
                continue;
            }
            System.out.println("Unknown command");
        }
    }
}
