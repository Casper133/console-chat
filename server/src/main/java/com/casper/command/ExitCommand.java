package com.casper.command;

import com.casper.Server;
import com.casper.session.ChatSession;
import com.casper.user.User;

import java.util.List;

public class ExitCommand implements Message {
    private User user;
    private Server server;

    ExitCommand(User user, Server server) {
        this.user = user;
        this.server = server;
    }

    @Override
    public void execute() {
        List<User> allUsers = server.getAllUsers();
        allUsers.remove(user);
        server.setAllUsers(allUsers);
        user.setUserExit(true);

        ChatSession chatSession = user.getChatSession();
        if (chatSession != null) {
            user.getChatSession().disconnect(user);
        }

        System.out.println(user.getUsername() + " отключился");

        user.closeAll();
    }
}
