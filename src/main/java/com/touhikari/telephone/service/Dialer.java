package com.touhikari.telephone.service;

public class Dialer {

    private static final int MIN_LEN = 3;
    private static final int MAX_LEN = 11;
    private final StringBuilder buffer = new StringBuilder();

    public void addDigit(char d) {
        if (Character.isDigit(d) && buffer.length() < MAX_LEN) {
            buffer.append(d);
        }
    }

    public void clear() {
        buffer.setLength(0);
    }

    public String getNumber() {
        return buffer.toString();
    }

    public boolean isValid(String number) {
        if (number == null) {
            return false;
        }
        int len = number.length();
        if (len < MIN_LEN || len > MAX_LEN) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            if (!Character.isDigit(number.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
