package com.touhikari.telephone.network;

public class NetworkSwitch {

    public Connection connect(String number) {
        if (isBusy(number)) {
            return new Connection(null, ConnectionStatus.BUSY);
        }
        Endpoint remote = new Endpoint();
        remote.ring();
        return new Connection(remote, ConnectionStatus.TRYING);
    }

    public boolean isBusy(String number) {
        return number == null || number.isEmpty() || "101".equals(number)
                || "102".equals(number) || number.startsWith("888")
                || number.endsWith("00");
    }
}
