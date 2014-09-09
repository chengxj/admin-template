package com.edgar.core.cache;

import org.springframework.context.ApplicationEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 14-9-9
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class VoidEvent extends ApplicationEvent {
    public VoidEvent(Object source) {
        super(source);
    }
}
