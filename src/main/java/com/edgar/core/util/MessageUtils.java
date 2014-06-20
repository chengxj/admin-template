package com.edgar.core.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

@Service
public class MessageUtils {
        private static MessageSource MESSAGE_SOURCE;

        private static LocaleResolver LOCAL_RESOLVER;

        @Autowired
        public void setMessageSource(MessageSource messageSource) {
                MESSAGE_SOURCE = messageSource;
        }

        @Autowired
        public void setLocaleResolver(LocaleResolver localeResolver) {
                LOCAL_RESOLVER = localeResolver;
        }

        public static String getMessage(String code, Object ... args) {
                Locale locale = getLocale();
                if (args == null) {
                        args = new Object[] {};
                }
                return MESSAGE_SOURCE.getMessage(code, args, locale);
        }

        private static Locale getLocale() {
                Locale locale = Locale.ENGLISH;
                try {
                        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                                        .currentRequestAttributes()).getRequest();
                        return LOCAL_RESOLVER.resolveLocale(request);
                } catch (Exception e) {
                        return locale;
                }
        }
}
