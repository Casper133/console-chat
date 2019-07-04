package com.casper.connector;

import com.casper.user.User;

public interface ClientConnector {
    void initConnection();

    void sendMessage(User user, String message);

    void disconnect();
}
