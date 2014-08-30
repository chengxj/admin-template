package com.edgar.core.auth;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class AuthenticationRequestWrapper
        extends HttpServletRequestWrapper {

    private final String body;

    public AuthenticationRequestWrapper(HttpServletRequest request) throws IOException {

        super(request);

        // read the original payload into the xmlPayload variable
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            // read the payload into the StringBuilder
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                // make an empty string since there is no payload
                stringBuilder.append("");
            }
        } catch (IOException ignored) {

        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException iox) {
                    // ignore
                }
            }
        }
        body = stringBuilder.toString();
    }

    /**
     * Override of the getInputStream() method which returns an InputStream that reads from the
     * stored XML payload string instead of from the request's actual InputStream.
     */
    @Override
    public ServletInputStream getInputStream()
            throws IOException {

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {
            public int read()
                    throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    public String getBody() {
        return body;
    }
}