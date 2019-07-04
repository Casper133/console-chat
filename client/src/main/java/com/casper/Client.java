package com.casper;

import com.casper.abstraction.AbstractClient;
import com.casper.connector.ServerConnector;
import com.casper.connector.SocketServerConnector;
import com.casper.reader.ConsoleMessageReader;
import com.casper.reader.MessageReader;
import com.casper.reader.ServerReader;

public class Client extends AbstractClient {
    private ServerConnector serverConnector = new SocketServerConnector();
    private MessageReader messageReader = new ConsoleMessageReader();
    private boolean clientStopped = false;

    Client() {
        System.out.println("Добро пожаловать!\n");
        System.out.println("Для начала войдите в чат (/reg USERNAME).");
        System.out.println("В любой момент можно выйти (/exit).\n");

        connectToServer();

        ServerReader serverReader = new ServerReader(this);
        Thread serverReaderThread = new Thread(serverReader);
        serverReaderThread.start();

        while (!clientStopped) {
            sendMessage();
        }
    }

    @Override
    public void connectToServer() {
        serverConnector.connect();
    }

    @Override
    public void sendMessage() {
        String message = messageReader.readMessage();

        if (message != null) {
            System.out.println();

            serverConnector.sendMessage(message);

            if (message.equals("/exit")) {
                System.out.println("Удачного дня!");
                closeAll();
            }
        }
    }

    @Override
    public void showMessage() {
        String message = serverConnector.readMessage();

        if (message != null) {
            System.out.println(message);
        }
    }

    private void closeAll() {
        serverConnector.disconnect();
        messageReader.stopReading();
        clientStopped = true;
        System.exit(0);
    }

    public boolean isClientStopped() {
        return clientStopped;
    }

    public void setClientStopped(boolean clientStopped) {
        this.clientStopped = clientStopped;
    }
}
