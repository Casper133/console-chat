package com.casper.session;

import com.casper.connector.SocketClientConnector;
import com.casper.user.User;

public class ChatSession {
    private User firstUser;
    private User secondUser;
    private SocketClientConnector clientConnector;

    public ChatSession(User firstUser, User secondUser, SocketClientConnector clientConnector) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.clientConnector = clientConnector;
        System.out.println("Создана чат-сессия между " + firstUser.getUsername() + " и " + secondUser.getUsername());
    }

    public void sendMessageInChatSession(User user, String message) {
        message = "[" + user.getUsername() + "]: " + message;
        clientConnector.sendMessage(getInterlocutor(user), message);
        System.out.println(message);
    }

    public void disconnect(User user) {
        firstUser.setInChatSession(false);
        secondUser.setInChatSession(false);

        User interlocutor = getInterlocutor(user);
        clientConnector.sendMessage(interlocutor,
                user.getUsername() + " отключился. Вы помещены в очередь ожидания собеседника\n");
        System.out.println("Разорвана чат-сессия между " + user.getUsername() + " и " + interlocutor.getUsername());
        interlocutor.setChatSession(null);
        clientConnector.createChatSession(interlocutor);
    }

    private User getInterlocutor(User user) {
        if (user.equals(firstUser)) {
            return secondUser;
        } else {
            return firstUser;
        }
    }
}
