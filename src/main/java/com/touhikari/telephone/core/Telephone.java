package com.touhikari.telephone.core;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Telephone {

    private Handset handset;
    private static CallController controller;

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
        try (Scanner sc = new Scanner(System.in, "UTF-8")) {
            System.out.println("Telephone Simulator");
            System.out.println("Type 'help' for commands");
            boolean running = true;
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> {
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
            }, 0, 200, TimeUnit.MILLISECONDS);
            while (running) {
                System.out.print("> ");
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s+", 2);
                String cmd = parts[0].toLowerCase();
                if ("help".equals(cmd)) {
                    System.out.println("Commands: pickup, hangup, digit <0-9>, call <number>, status, numbers, addinfo <number> <durationMs> <text>, delinfo <number>, listinfo, busy <number>, free <number>, listbusy, answer, remotehangup, timeout, clear, exit");
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
                if ("addinfo".equals(cmd)) {
                    if (parts.length < 2) {
                        System.out.println("Usage: addinfo <number> <durationMs> <text>");
                        continue;
                    }
                    String[] a = parts[1].split("\\s+", 3);
                    if (a.length < 3) {
                        System.out.println("Usage: addinfo <number> <durationMs> <text>");
                        continue;
                    }
                    String num = a[0];
                    int dur;
                    try {
                        dur = Integer.parseInt(a[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid duration");
                        continue;
                    }
                    String text = a[2];
                    controller.getMessageStore().putMessage(num, text, dur);
                    System.out.println("Info added for " + num);
                    continue;
                }
                if ("delinfo".equals(cmd)) {
                    if (parts.length < 2) {
                        System.out.println("Usage: delinfo <number>");
                        continue;
                    }
                    String num = parts[1].trim();
                    controller.getMessageStore().removeMessage(num);
                    System.out.println("Info removed for " + num);
                    continue;
                }
                if ("listinfo".equals(cmd)) {
                    System.out.println("Info numbers:");
                    for (String k : controller.getMessageStore().keys()) {
                        System.out.println(k);
                    }
                    continue;
                }
                if ("busy".equals(cmd)) {
                    if (parts.length < 2) {
                        System.out.println("Usage: busy <number>");
                        continue;
                    }
                    String num = parts[1].trim();
                    controller.getNetworkSwitch().addBusy(num);
                    System.out.println("Marked busy: " + num);
                    continue;
                }
                if ("free".equals(cmd)) {
                    if (parts.length < 2) {
                        System.out.println("Usage: free <number>");
                        continue;
                    }
                    String num = parts[1].trim();
                    controller.getNetworkSwitch().removeBusy(num);
                    System.out.println("Marked free: " + num);
                    continue;
                }
                if ("listbusy".equals(cmd)) {
                    System.out.println("Busy numbers:");
                    for (String k : controller.getNetworkSwitch().getBusyNumbers()) {
                        System.out.println(k);
                    }
                    continue;
                }
                if ("clear".equals(cmd)) {
                    controller.getDialer().clear();
                    System.out.println("Dialer cleared");
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
                    scheduler.shutdownNow();
                    continue;
                }
                System.out.println("Unknown command");
            }
            scheduler.shutdownNow();
        }
    }
}
