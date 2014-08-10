package com.edgar.core.shiro;

import org.apache.shiro.authc.ExcessiveAttemptsException;

/**
 * 密码错误次数过多的异常
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public class CustomExcessiveAttemptsException extends ExcessiveAttemptsException {

        private static final long serialVersionUID = 7632674360113020737L;

        /**
         * 错误次数
         */
        private final int attemptsNum;

        public CustomExcessiveAttemptsException(int attemptsNum) {
                super();
                this.attemptsNum = attemptsNum;
        }

        public int getAttemptsNum() {
                return attemptsNum;
        }

}
