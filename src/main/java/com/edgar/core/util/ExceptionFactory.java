package com.edgar.core.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.edgar.core.exception.BusinessCode;
import com.edgar.core.exception.SystemException;
import com.edgar.core.shiro.AuthContextHolder;

/**
 * 创建异常的工厂类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public abstract class ExceptionFactory {

        /**
         * 用户未登录
         * 
         * @return SystemException
         */
        public static SystemException unLogin() {
                return new SystemException(BusinessCode.UNLOGIN, getMessage("msg.error.unlogin"));
        }

        /**
         * 用户名/密码错误
         * 
         * @return SystemException
         */
        public static SystemException userOrPasswordError() {
                int retryCount = AuthContextHolder.getRetryCount();
                return new SystemException(BusinessCode.USERNAME_PASSWORD_ERROR, getMessage(
                                "msg.error.userOrPasswordError", 10 - retryCount));
        }

        /**
         * 没有访问权限
         * 
         * @return SystemException
         */
        public static SystemException unAuthorized() {
                return new SystemException(BusinessCode.UNAUTHORIZED,
                                getMessage("msg.error.unAuthorized"));
        }

        /**
         * 请求被禁止
         * 
         * @param code
         *                异常消息的国际化键
         * @param args
         *                参数
         * @return SystemException
         */
        public static SystemException forbidden(String code, Object... args) {
                return new SystemException(BusinessCode.FORBIDDEN, getMessage(code, args));
        }

        /**
         * 密码错误次数过多
         * 
         * @param num
         *                错误次数
         * 
         * @return SystemException
         */
        public static SystemException ExcessiveAttempts(int num) {
                return new SystemException(BusinessCode.EXCESSIVE_ATTEMPTS,
                                getMessage("msg.error.excessiveAttempts")).setProperty(
                                "ExcessiveAttempts", num);
        }

        /**
         * 空
         * 
         * @param code
         *                异常消息的国际化键
         * @param args
         *                参数
         * @return SystemException
         */
        public static SystemException isNull(String code, Object... args) {
                return new SystemException(BusinessCode.NULL, getMessage(code, args));
        }

        /**
         * 重复值
         * 
         * @param code
         *                异常消息的国际化键
         * @param args
         *                参数
         * @return SystemException
         */
        public static SystemException duplicate(String code, Object... args) {
                return new SystemException(BusinessCode.DUPLICATE, getMessage(code, args));
        }

        /**
         * 数据过期
         * 
         * @return SystemException
         */
        public static SystemException expired() {
                return new SystemException(BusinessCode.EXPIRED, getMessage("msg.error.expired"));
        }

        /**
         * 资源不存在
         * 
         * @return SystemException
         */
        public static SystemException notFound() {
                return new SystemException(BusinessCode.NOT_FOUNT, getMessage("msg.error.notFound"));
        }

        /**
         * 请求非法
         * 
         * @return SystemException
         */
        public static SystemException badRequest() {
                return new SystemException(BusinessCode.BAD_REQUEST,
                                getMessage("msg.error.badRequest"));
        }

        /**
         * 不支持的请求
         * 
         * @return SystemException
         */
        public static SystemException unSupportMethod() {
                return new SystemException(BusinessCode.UNSUPPORT_METHOD,
                                getMessage("msg.error.unSupportMethod"));
        }

        /**
         * 系统错误
         * 
         * @return SystemException
         */
        public static SystemException appError() {
                return new SystemException(BusinessCode.APP_ERROR, getMessage("msg.error.appError"));
        }

        /**
         * 作业错误
         * 
         * @param
         * @return SystemException
         */
        public static SystemException job() {
                return new SystemException(BusinessCode.JOB, getMessage("msg.error.job"));
        }

        /**
         * 根据spring的valid校验结果创建SystemException
         * 
         * @param result
         *                BindingResult
         * @return SystemException
         */
        public static SystemException invalidBindResult(BindingResult result) {
                SystemException e = new SystemException(BusinessCode.INVALID,
                                getMessage("msg.error.invalidBindResult"));
                if (result.hasErrors()) {
                        List<FieldError> errors = result.getFieldErrors();
                        for (FieldError fieldError : errors) {
                                e.setProperty(fieldError.getField(), fieldError.getField()
                                                + fieldError.getDefaultMessage());
                        }
                }
                return e;
        }

        /**
         * 根据jsr303的valid校验结果创建SystemException
         * 
         * @param constraintViolations
         *                校验结果的结果集
         * @return SystemException
         */
        public static SystemException inValid(Set<ConstraintViolation<Object>> constraintViolations) {
                SystemException e = new SystemException(BusinessCode.INVALID,
                                getMessage("msg.error.invalidBindResult"));
                for (ConstraintViolation<?> violation : constraintViolations) {
                        String message = replaceMessageArg(violation);
                        e.setProperty(violation.getPropertyPath().toString(), message);
                }
                return e;
        }

        private static String replaceMessageArg(ConstraintViolation<?> violation) {
                String messageTemplate = StringUtils.substringBetween(
                                violation.getMessageTemplate(), "{", "}");
                String message = getMessage(messageTemplate);
                Map<String, Object> attributes = violation.getConstraintDescriptor()
                                .getAttributes();
                if (attributes.containsKey("min")) {
                        String min = attributes.get("min").toString();
                        message = StringUtils.replace(message, "{min}", min);
                }
                if (attributes.containsKey("max")) {
                        String max = attributes.get("max").toString();
                        message = StringUtils.replace(message, "{max}", max);
                }
                if (attributes.containsKey("value")) {
                        String value = attributes.get("value").toString();
                        message = StringUtils.replace(message, "{value}", value);
                }
                if (attributes.containsKey("regexp")) {
                        String regexp = attributes.get("regexp").toString();
                        message = StringUtils.replace(message, "{regexp}", regexp);
                }
                if (attributes.containsKey("script")) {
                        String script = attributes.get("script").toString();
                        message = StringUtils.replace(message, "{script}", script);
                }
                if (attributes.containsKey("integer")) {
                        String integer = attributes.get("integer").toString();
                        message = StringUtils.replace(message, "{integer}", integer);
                }
                if (attributes.containsKey("fraction")) {
                        String integer = attributes.get("fraction").toString();
                        message = StringUtils.replace(message, "{fraction}", integer);
                }
                return message;
        }

        /**
         * 参数错误
         * 
         * @param msg
         *                错误提示
         * @return SystemException
         */
        public static SystemException inValidParameter(String code) {
                SystemException e = new SystemException(BusinessCode.INVALID_PARAMETER,
                                getMessage(code));
                return e;
        }

        private static String getMessage(String key, Object... args) {
                return MessageUtils.getMessage(key, args);
        }
}
