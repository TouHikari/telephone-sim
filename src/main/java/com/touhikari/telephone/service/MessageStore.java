package com.touhikari.telephone.service;

import java.util.HashMap;
import java.util.Map;

import com.touhikari.telephone.model.Message;

public class MessageStore {

    private final Map<String, Message> store = new HashMap<>();

    public MessageStore() {
        Message m1 = new Message();
        m1.text = "您拨打的号码不存在，请检查后重试";
        m1.durationMs = 3000;
        store.put("404", m1);

        Message m2 = new Message();
        m2.text = "您拨打的号码正在通话中，请稍后再拨";
        m2.durationMs = 2500;
        store.put("500", m2);

        Message m3 = new Message();
        m3.text = "该服务暂未开通";
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
