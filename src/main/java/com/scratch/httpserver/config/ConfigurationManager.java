package com.scratch.httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.scratch.httpserver.util.Json;
import com.sun.net.httpserver.HttpsConfigurator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// this class will be used to load the configuration file
// this is a singleton class
public class ConfigurationManager {

    private static ConfigurationManager myConfigurationManager;
    private static Configuration myCurrentConfiguration;

    private ConfigurationManager(){};

    public static ConfigurationManager getInstance(){
        if(myConfigurationManager == null)
            myConfigurationManager = new ConfigurationManager();
        return myConfigurationManager;
    }

    // used to load the configuration file by the provided path
    public void loadConfigurationFile(String filePath) throws RuntimeException {

//        Originally these lines were:
//        FileReader fileReader;
//        try {
//            fileReader = new FileReader(filePath);
//        } catch (FileNotFoundException e) {
//            throw new HttpConfigurationException(e);
//        }
////        Originally StringBuffer changed to StringBuilder.
//        StringBuilder stringBuffer = new StringBuilder();
//        int i;
//        try {
//            while((i = fileReader.read()) != -1){
//                stringBuffer.append((char) i);
//            }
//        } catch (IOException e) {
//            throw new HttpConfigurationException(e);
//        }

        StringBuilder stringBuffer = getStringBuilder(filePath);
        
        JsonNode conf = null;
        try {
            // parse the json string and return a JsonNode
            conf = Json.parse(stringBuffer.toString());
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing the configuration");
        }
        try {
            // convert the JsonNode to an object of the Configuration class
            myCurrentConfiguration = Json.fromJson(conf, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing the configuration, internally");
        }
    }

    private static StringBuilder getStringBuilder(String filePath) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException(e);
        }
//        Originally StringBuffer changed to StringBuilder.
        StringBuilder stringBuffer = new StringBuilder();
        int i;
        try {
            while((i = fileReader.read()) != -1){
                stringBuffer.append((char) i);
            }
        } catch (IOException e) {
            throw new HttpConfigurationException(e);
        }
        return stringBuffer;
    }

    // returns the current configuration
    public Configuration getCurrentConfiguration(){
        if(myCurrentConfiguration == null){
            throw new HttpConfigurationException("No Configuration set.");
        }
        return myCurrentConfiguration;
    }

}
