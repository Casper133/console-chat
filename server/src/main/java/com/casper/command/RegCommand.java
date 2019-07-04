package com.casper.command;

import com.casper.connector.SocketClientConnector;
import com.casper.user.User;

public class RegCommand implements Message {
    private User user;
    private String message;
    private SocketClientConnector clientConnector;

    RegCommand(User user, String message, SocketClientConnector clientConnector) {
        this.user = user;
        this.message = message;
        this.clientConnector = clientConnector;
    }

    @Override
    public void execute() {
        if (!user.isLoggedIn()) {
            String username = message.replaceAll("/reg ", "");
            user.setUsername(username);
            user.setLoggedIn(true);
            clientConnector.sendMessage(user, "Регистрация прошла успешно! " +
                    "Вы помещены в очередь ожидания собеседника\n");
            System.out.println("Пользователь " + username + " только что зарегистрировался");
            clientConnector.createChatSession(user);
        } else {
            clientConnector.sendMessage(user, "Вы уже зарегистрированы!\n");
        }
    }
}
