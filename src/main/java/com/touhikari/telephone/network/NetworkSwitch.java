package com.touhikari.telephone.network;

import java.util.HashSet;
import java.util.Set;

public class NetworkSwitch {

    private final Set<String> userBusy = new HashSet<>();

    public Connection connect(String number) {
        if (isBusy(number)) {
            return new Connection(null, ConnectionStatus.BUSY);
        }
        if ("111".equals(number)) {
            Endpoint remote = new Endpoint();
            remote.answer();
            return new Connection(remote, ConnectionStatus.CONNECTED);
        }
        Endpoint remote = new Endpoint();
        remote.ring();
        return new Connection(remote, ConnectionStatus.TRYING);
    }

    public boolean isBusy(String number) {
        return number == null || number.isEmpty() || userBusy.contains(number) || "101".equals(number)
                || "102".equals(number) || "555".equals(number) || number.startsWith("888")
                || number.endsWith("00");
    }

    public void addBusy(String number) {
        if (number != null && !number.isEmpty()) {
            userBusy.add(number);
        }
    }

    public void removeBusy(String number) {
        if (number != null) {
            userBusy.remove(number);
        }
    }

    public Set<String> getBusyNumbers() {
        return userBusy;
    }
}
