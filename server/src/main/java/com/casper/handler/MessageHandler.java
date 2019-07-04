package com.casper.handler;

import com.casper.Server;
import com.casper.command.MessageInvoker;
import com.casper.user.User;

import java.io.BufferedReader;
import java.io.IOException;

public class MessageHandler implements Runnable {
    private User user;
    private Server server;

    MessageHandler(User user, Server server) {
        this.user = user;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BufferedReader inReader = user.getInReader();
            while (!user.isUserExit()) {
                String message = inReader.readLine();
                if (message != null) {
                    MessageInvoker messageInvoker = new MessageInvoker(user, server, message);
                    messageInvoker.handle();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
