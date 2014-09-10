package com.edgar.core.guava;

import com.google.common.base.CharMatcher;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-9-10
 * Time: 下午5:06
 * To change this template use File | Settings | File Templates.
 */
public class StringTest {

    @Test
    public void testWhiteSpace() {
        Assert.assertTrue( !CharMatcher.WHITESPACE.matchesAllOf("   "));
    }
}
