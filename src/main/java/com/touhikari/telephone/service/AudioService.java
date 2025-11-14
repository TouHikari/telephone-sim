package com.touhikari.telephone.service;

import com.touhikari.telephone.model.Message;
import com.touhikari.telephone.network.Connection;

public class AudioService {

    private boolean dialTonePlaying;
    private boolean busyTonePlaying;
    private boolean beepPlaying;
    private boolean ringing;
    private boolean voiceActive;
    private boolean infoPlaying;
    private Connection voiceChannel;
    private Message currentMessage;
    private Timer infoTimer;

    public void playDialTone() {
        if (dialTonePlaying) {
            return;
        }
        stopBusyToneInternal();
        stopBeepInternal();
        stopRinging();
        stopVoice();
        stopInfo();
        dialTonePlaying = true;
    }

    public void stopDialTone() {
        if (!dialTonePlaying) {
            return;
        }
        dialTonePlaying = false;
    }

    public void playBusyTone() {
        if (busyTonePlaying) {
            return;
        }
        stopDialTone();
        stopBeepInternal();
        stopRinging();
        stopVoice();
        stopInfo();
        busyTonePlaying = true;
    }

    public void stopBusyTone() {
        stopBusyToneInternal();
    }

    public void playBeep() {
        if (beepPlaying) {
            return;
        }
        stopDialTone();
        stopBusyToneInternal();
        stopRinging();
        stopVoice();
        stopInfo();
        beepPlaying = true;
    }

    public void stopBeep() {
        stopBeepInternal();
    }

    public void startRinging() {
        if (ringing) {
            return;
        }
        stopDialTone();
        stopBusyToneInternal();
        stopBeepInternal();
        stopVoice();
        stopInfo();
        ringing = true;
    }

    public void stopRinging() {
        if (!ringing) {
            return;
        }
        ringing = false;
    }

    public void startVoice(Connection channel) {
        if (voiceActive && voiceChannel == channel) {
            return;
        }
        stopDialTone();
        stopBusyToneInternal();
        stopBeepInternal();
        stopRinging();
        stopInfo();
        voiceActive = true;
        voiceChannel = channel;
    }

    public void stopVoice() {
        if (!voiceActive) {
            return;
        }
        voiceActive = false;
        voiceChannel = null;
    }

    public void playInfo(Message message) {
        if (infoPlaying && currentMessage != null) {
            boolean same = false;
            if (currentMessage.text == null && message.text == null) {
                same = currentMessage.durationMs == message.durationMs;
            } else if (currentMessage.text != null && message.text != null) {
                same = currentMessage.text.equals(message.text) && currentMessage.durationMs == message.durationMs;
            }
            if (same) {
                return;
            }
        }
        stopDialTone();
        stopBusyToneInternal();
        stopBeepInternal();
        stopRinging();
        stopVoice();
        currentMessage = message;
        infoPlaying = true;
        if (infoTimer == null) {
            infoTimer = new Timer();
        }
        infoTimer.reset();
        infoTimer.start();
    }

    public void stopInfo() {
        if (!infoPlaying) {
            return;
        }
        infoPlaying = false;
        currentMessage = null;
        if (infoTimer != null) {
            infoTimer.stop();
        }
    }

    private void stopBusyToneInternal() {
        if (!busyTonePlaying) {
            return;
        }
        busyTonePlaying = false;
    }

    private void stopBeepInternal() {
        if (!beepPlaying) {
            return;
        }
        beepPlaying = false;
    }

    public boolean isDialTonePlaying() {
        return dialTonePlaying;
    }

    public boolean isBusyTonePlaying() {
        return busyTonePlaying;
    }

    public boolean isBeepPlaying() {
        return beepPlaying;
    }

    public boolean isRinging() {
        return ringing;
    }

    public boolean isVoiceActive() {
        return voiceActive;
    }

    public boolean isInfoPlaying() {
        return infoPlaying;
    }

    public Connection getVoiceChannel() {
        return voiceChannel;
    }

    public Message getCurrentMessage() {
        return currentMessage;
    }
}
