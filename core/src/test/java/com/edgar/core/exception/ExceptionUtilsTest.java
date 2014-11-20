package com.edgar.core.exception;

import com.edgar.core.util.ExceptionUtils;
import org.junit.Test;

/**
 * Created by Administrator on 2014/11/17.
 */
public class ExceptionUtilsTest {

    @Test
    public void testIgnore() {
        doSomething();
    }

    @Test(expected = AppException.class)
    public void testThrwoAppException() {
        doSomething1();
    }

    private void doSomething1() {
        try {
            doSomething2();
        } catch (Exception e){
            new ThrowAppException().handle("doSomething1", "Null", "空指针1", e);
        }
    }

    private void doSomething2() {
        try {
            throw new NullPointerException();
        } catch (Exception e){
            new ThrowAppException().handle("doSomething2", "Null", "空指针2", e);
        }
    }

    private void doSomething() {
        try {
            throw new NullPointerException();
        } catch (Exception e){
            ExceptionUtils.ignore("doSomething", "Null", "空指针", e);
        }
    }
}
