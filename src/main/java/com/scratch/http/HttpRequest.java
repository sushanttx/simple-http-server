package com.scratch.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion; //literal from request
    private HttpVersion bestCompatibleVersion;
    private Map<String, String> headers = new HashMap<>();

    HttpRequest() {
    }

    public String getRequestTarget() { return requestTarget; }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpVersion getBestCompatibleVersion() {
        return bestCompatibleVersion;
    }

    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }

    void setMethod(String methodName) throws HttpParsingException {
        for(HttpMethod method : HttpMethod.values()){
            if(methodName.equals(method.name())){
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException(
                HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED
        );
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if(requestTarget == null || requestTarget.isEmpty()){
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }

    void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);
        if(this.bestCompatibleVersion == null){
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    public String getHeader(String headerName){
        return headers.get(headerName.toLowerCase());
    }

    void addHeaders(String headerName, String headerField){
        headers.put(headerName.toLowerCase(), headerField);
    }
}
