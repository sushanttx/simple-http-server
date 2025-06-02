package com.scratch.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; //32
    private static final int CR = 0x0D; //13
    private static final int LF = 0x0A; //10

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException{
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest httpRequest = new HttpRequest();

        try {
            parseRequestLine(reader, httpRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            parseHeaders(reader, httpRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        parseBody(reader, httpRequest);

        return httpRequest;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest httpRequest) throws IOException, HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder();

        boolean methodParsed = false;
        boolean requestTargetParsed = false;


        int _byte;
        while((_byte = reader.read()) >= 0){
            if (_byte == CR){
                _byte = reader.read();
                if(_byte == LF){
                    LOGGER.debug("Request Line VERSION to Process : {}", processingDataBuffer.toString());
                    if(!methodParsed || !requestTargetParsed){
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }

                    try {
                        httpRequest.setHttpVersion(processingDataBuffer.toString());
                    } catch (BadHttpVersionException e) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }


                    return;
                } else{
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            if(_byte == SP){
                if(!(methodParsed)){
                    LOGGER.debug("Request Line METHOD to Process : {}", processingDataBuffer.toString());
                    httpRequest.setMethod(processingDataBuffer.toString());
                    methodParsed = true;
                } else if (!(requestTargetParsed)) {
                    LOGGER.debug("Request Line TARGET to Process : {}", processingDataBuffer.toString());
                    httpRequest.setRequestTarget(processingDataBuffer.toString());
                    requestTargetParsed = true;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                processingDataBuffer.delete(0, processingDataBuffer.length());
            }else {
                processingDataBuffer.append((char) _byte);
                if(!methodParsed){
                    if(processingDataBuffer.length() > HttpMethod.MAX_LENGTH){
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest httpRequest) throws IOException, HttpParsingException {
        boolean crlfFound = false;
        StringBuilder processingDataBuffer = new StringBuilder();
        int _byte;
        while ((_byte = reader.read()) >=0) {
            if (_byte == CR) {
                _byte = reader.read();
                if (_byte == LF) {
                    if (!crlfFound) {
                        crlfFound = true;

                        // Do Things like processing
                        processingSingleHeaderField(processingDataBuffer, httpRequest);
                        // Clear the buffer
                        processingDataBuffer.delete(0, processingDataBuffer.length());
                    } else {
                        // Two CRLF received, end of Headers section
                        return;
                    }
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            } else {
                crlfFound = false;
                // Append to Buffer
                processingDataBuffer.append((char)_byte);
            }
        }
    }

    private void processingSingleHeaderField(StringBuilder processingDataBuffer, HttpRequest httpRequest) throws HttpParsingException {
        String rawHeaderField = processingDataBuffer.toString();
        Pattern pattern = Pattern.compile("^(?<fieldName>[!#$%&'*+\\-.^_`|~\\dA-Za-z]+):\\s*(?<fieldValue>[^\\r\\n]+)\\s*$");

        Matcher matcher = pattern.matcher(rawHeaderField);
        if(matcher.matches()){
            String fieldName = matcher.group("fieldName");
            String fieldValue = matcher.group("fieldValue");
            httpRequest.addHeaders(fieldName, fieldValue);
        } else {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    private void parseBody(InputStreamReader reader, HttpRequest httpRequest) {
    }


}
