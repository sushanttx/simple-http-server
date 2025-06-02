package com.scratch.httpserver;

import com.scratch.httpserver.config.Configuration;
import com.scratch.httpserver.config.ConfigurationManager;
import com.scratch.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {

        LOGGER.info("Server starting...");

        // load the configuration file
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Using port: "+ conf.getPort());
        LOGGER.info("Using Webroot: "+ conf.getWebroot());

        try {
            // create a new server listener thread with the port and webroot. the thread will listen for incoming connections on the port and serve the webroot directory. this will help our main thread to handle multiple requests concurrently
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            // start the server listener thread
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
