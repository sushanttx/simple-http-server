package com.scratch.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerListenerThread extends Thread{
    private int port;
    private String webRoot;
    private ServerSocket serverSocket;
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

    public ServerListenerThread(int port, String webRoot) throws IOException {
        this.port = port;
        this.webRoot = webRoot;
        this.serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {
        try{
            // while the server socket is bound(which means it is listening for incoming connections from the connected client), accept incoming connections. the server socket has got a queue, thus if a thread is executing, others in the queue will wait. each of those clients are processesed in a series.
            while (serverSocket.isBound() && !serverSocket.isClosed()){

                // accept incoming connections
                Socket socket = serverSocket.accept();

                // log the connection. the inetaddress is the ip address of the client
                LOGGER.info("Connection Accepted"+ socket.getInetAddress());

                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket);
                workerThread.start();
            }

        } catch (IOException e) {
            LOGGER.error("Problem with setting socket", e);
        } finally{
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {}
            }
        }
    }
}
