package com.edgar.core.validator;

/**
 * 校验类的接口
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public interface ValidatorStrategy {

        /**
         * 校验对象是否合法
         * 
         * @param target
         *                需要校验的对象
         * @return 如果通过校验，返回true
         */
        boolean validator(Object target);

}