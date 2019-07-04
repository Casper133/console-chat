package com.casper.connector;

import com.casper.Server;
import com.casper.session.ChatSession;
import com.casper.user.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

public class SocketClientConnector implements ClientConnector {
    private ServerSocket serverSocket;
    private Server server;

    public SocketClientConnector(Server server) {
        this.server = server;
    }

    @Override
    public void initConnection() {
        final int port = 5555;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createChatSession(User firstUser) {
        List<User> allUsers = server.getAllUsers();
        if (allUsers.size() >= 2) {
            for (User secondUser : allUsers) {
                if (!secondUser.isInChatSession() && secondUser.isLoggedIn() && !secondUser.equals(firstUser)) {
                    firstUser.setInChatSession(true);
                    secondUser.setInChatSession(true);

                    ChatSession chatSession = new ChatSession(firstUser, secondUser, this);
                    firstUser.setChatSession(chatSession);
                    secondUser.setChatSession(chatSession);

                    sendMessage(firstUser, "Вы подключены к " + secondUser.getUsername() + "\n");
                    sendMessage(secondUser, "Вы подключены к " + firstUser.getUsername() + "\n");

                    List<String> userMessages = firstUser.getMessages();
                    if (userMessages.size() > 0) {
                        for (String message : userMessages) {
                            chatSession.sendMessageInChatSession(firstUser, message);
                        }
                    }

                    userMessages = secondUser.getMessages();
                    if (userMessages.size() > 0) {
                        for (String message : userMessages) {
                            chatSession.sendMessageInChatSession(secondUser, message);
                        }
                    }

                    break;
                }
            }
        }
    }

    @Override
    public void sendMessage(User user, String message) {
        try {
            BufferedWriter outWriter = user.getOutWriter();
            outWriter.write(message);
            outWriter.newLine();
            outWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }

            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
