package com.edgar.core.shiro;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.TeeInputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class CustomJsonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    public static final String REQUEST_BODY_ATTRIBUTE_NAME = "key.to.requestBody";


    @Override
    public Object read(Type type, Class<?> contextClass, final HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        final ByteArrayOutputStream writerStream = new ByteArrayOutputStream();

        HttpInputMessage message = new HttpInputMessage() {
            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }

            @Override
            public InputStream getBody() throws IOException {
                return new TeeInputStream(inputMessage.getBody(), writerStream);
            }
        };
        Object result = super.read(type, contextClass, message);
        RequestContextHolder.currentRequestAttributes().setAttribute("edgarabcd", writerStream, RequestAttributes.SCOPE_REQUEST);

//        System.out.println(writerStream.toString());
        
        return result;
    }

}