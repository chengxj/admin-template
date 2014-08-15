package com.edgar.core.shiro;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.TeeInputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

    private final ByteArrayOutputStream writerStream = new ByteArrayOutputStream();

    public MultiReadHttpServletRequest(HttpServletRequest httpServletRequest) throws IOException {
        super(httpServletRequest);
        IOUtils.copy(httpServletRequest.getInputStream(),writerStream);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
//        IOUtils.copy(writerStream)
        return new ServletInputStreamImpl(super.getInputStream());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        String enc = getCharacterEncoding();
        if(enc == null) enc = "UTF-8";
        return new BufferedReader(new InputStreamReader(getInputStream(), enc));
    }

}