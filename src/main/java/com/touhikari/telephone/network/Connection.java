package com.touhikari.telephone.network;

public class Connection {

    public ConnectionStatus status;
    public Endpoint remote;

    public Connection() {
        this.status = ConnectionStatus.TRYING;
    }

    public Connection(Endpoint remote) {
        this.remote = remote;
        this.status = ConnectionStatus.TRYING;
    }

    public Connection(Endpoint remote, ConnectionStatus status) {
        this.remote = remote;
        this.status = status;
    }

    public void disconnect() {
        status = ConnectionStatus.DISCONNECTED;
        if (remote != null) {
            remote.hangUp();
        }
    }
}
