package com.scratch.httpserver.config;

// this class will be mapped to the json file
public class Configuration {

    private int port;
    // the port number that the server will listen on
    
    private String webroot;
    // the place where the web server will store the files

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getWebroot() {
        return webroot;
    }

    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }
}
