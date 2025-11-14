package com.touhikari.telephone.service;

import java.util.HashMap;
import java.util.Map;

import com.touhikari.telephone.model.Message;

public class MessageStore {

    private final Map<String, Message> store = new HashMap<>();

    public MessageStore() {
        Message m1 = new Message();
        m1.text = "The number you dialed does not exist. Please check and try again.";
        m1.durationMs = 3000;
        store.put("404", m1);

        Message m2 = new Message();
        m2.text = "The number you dialed is currently busy. Please call later.";
        m2.durationMs = 2500;
        store.put("500", m2);

        Message m3 = new Message();
        m3.text = "The service is not available.";
        m3.durationMs = 2500;
        store.put("999", m3);
    }

    public boolean hasMessage(String number) {
        return store.containsKey(number);
    }

    public Message getMessage(String number) {
        return store.get(number);
    }
}
