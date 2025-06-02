package com.scratch.httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnectionWorkerThread extends Thread {
    private Socket socket;
    // this logger is used to log the messages to the console for the HttpConnectionWorkerThread class
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // get the input and output streams
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            // got request
            // int _byte;
            // while ((_byte = inputStream.read()) >= 0) {
            //     System.out.print((char) _byte);
            // }

            // create the html response
            String html = "<html><head><title>Simple Java Http Server</title></head><body><h1> This page is being loaded using pure Java Sockets</h1></body></html>";

            // create the response
            final String CRLF = "\n\r";
            String response = "HTTP/1.1 200 OK" + CRLF + "Content Length: "+ html.getBytes().length + CRLF+CRLF+html+CRLF+CRLF;

            // write the response to the output stream
            outputStream.write(response.getBytes());

            // Slept to simulate thread execution and check for multithreadedness

            LOGGER.info("Connection processed: "+ socket.getInetAddress());
        } catch (IOException e) {
            LOGGER.error("Problem with Connection", e);
        } finally{
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }
}
