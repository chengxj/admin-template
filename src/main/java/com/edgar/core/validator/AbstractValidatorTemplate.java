package com.edgar.core.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.edgar.core.util.ExceptionFactory;
import com.edgar.core.util.MessageUtils;

/**
 * 校验类模板类，所有校验都需要继承此类
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public abstract class AbstractValidatorTemplate implements ValidatorStrategy {

        private final Validator validator;

        public AbstractValidatorTemplate() {
                validator = createValidator();
        }

        /**
         * 创建一个JSR303的validator
         * 
         * @return Validator
         */
        public abstract Validator createValidator();

        @Override
        public final boolean validator(Object target) {
                Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target);
                if (!constraintViolations.isEmpty()) {
                        throw ExceptionFactory.inValid(constraintViolations);
                }
                return true;
        }
        
        protected String getMessage(String key, Object ... args) {
                return MessageUtils.getMessage(key, args);
        }
}
