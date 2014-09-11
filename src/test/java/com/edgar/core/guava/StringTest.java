package com.edgar.core.guava;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
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
        Assert.assertTrue(Strings.isNullOrEmpty("   "));
        Assert.assertTrue(Strings.isNullOrEmpty(null));
        Assert.assertTrue(Strings.isNullOrEmpty(""));
//        Assert.assertTrue(Strings.isNullOrEmpty("   "));
        Assert.assertTrue(StringUtils.isBlank("   "));
    }
}
