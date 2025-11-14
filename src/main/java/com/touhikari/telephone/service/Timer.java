package com.touhikari.telephone.service;

public class Timer {

    private long startAt;
    private long lastElapsed;
    private boolean running;

    public void reset() {
        running = false;
        startAt = 0L;
        lastElapsed = 0L;
    }

    public void start() {
        startAt = System.nanoTime();
        running = true;
    }

    public void stop() {
        if (running) {
            lastElapsed = (System.nanoTime() - startAt) / 1_000_000L;
            running = false;
        }
    }

    public int elapsed() {
        if (running) {
            long ms = (System.nanoTime() - startAt) / 1_000_000L;
            return (int) ms;
        }
        return (int) lastElapsed;
    }

    public boolean timeout(int ms) {
        return elapsed() >= ms;
    }
}
