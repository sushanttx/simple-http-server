package com.scratch.http;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HttpVersionTest {
    @Test
    void getBestCompatibleVersionExactMatch(){
        HttpVersion httpVersion = null;
        try {
            httpVersion = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
        } catch (BadHttpVersionException e) {
            e.printStackTrace();
            fail();
        }

        assertNotNull(httpVersion);
        assertEquals(httpVersion, HttpVersion.HTTP_1_1);
    }

    @Test
    void getBestCompatibleVersionBadFormat(){
        HttpVersion httpVersion = null;
        try {
            httpVersion = HttpVersion.getBestCompatibleVersion("http/1.1");
            fail();
        } catch (BadHttpVersionException e) {
        }
    }

    @Test
    void getBestCompatibleVersionHigher(){
        HttpVersion httpVersion = null;
        try {
            httpVersion = HttpVersion.getBestCompatibleVersion("HTTP/1.2");
            assertNotNull(httpVersion);
            assertEquals(httpVersion, HttpVersion.HTTP_1_1);
        } catch (BadHttpVersionException e) {
            fail();
        }
    }
}
