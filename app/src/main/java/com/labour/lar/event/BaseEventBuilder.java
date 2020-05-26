package com.labour.lar.event;

import java.util.HashMap;

/**
 * Created by lx on 2017/6/9.
 */
public abstract class BaseEventBuilder {

    protected static final HashMap<String, Class<? extends BaseEvent>> eventCache = new HashMap<>();

    protected HashMap<String, Class<? extends BaseEvent>> innerBuildEvents(){
        buildEvents();
        return eventCache;
    }

    /**
     * 注册系统事件避免事件发送乱
     */
    public abstract void buildEvents();

    /**
     * 对应关系
     * @param eventCls 事件类
     * @param subscribeCls 订阅者类
     * @param subscribeClsMethodName 订阅者类对应的方法
     */
    protected void build(Class<? extends BaseEvent> eventCls, Class<?> subscribeCls, String subscribeClsMethodName) {
        eventCache.put(subscribeCls.getSimpleName()+"_" +subscribeClsMethodName,eventCls);
    }

    public Class<? extends BaseEvent> getEvent(String key){
        return eventCache.get(key);
    }
}
