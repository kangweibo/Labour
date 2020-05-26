package com.labour.lar.event;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by lx on 2017/6/9.
 */
public class EventManager {

    private static String TAG = EventManager.class.getSimpleName();
    private static final String DEFAULT_SUBSCRIBE_METHOD_NAME = "onEventReceived";
    private static BaseEventBuilder eventBuilder;

    private EventManager(){}

    public static void init(BindEventBuilder buildEventBuilder){
        eventBuilder = buildEventBuilder.getEventBuilder();
        eventBuilder.buildEvents();
    }
    public static void register(Object subscriber){
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber){
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(BaseEvent event){
        EventBus.getDefault().post(event);
    }
    public static void post(Class<?> subscribeCls, String methodName){
        BaseEvent event = getEvent(subscribeCls, methodName);
        post(event);
    }
    public static void post(Class<?> subscribeCls, String methodName,Object data){
        BaseEvent event = getEvent(subscribeCls, methodName);
        event.setData(data);
        post(event);
    }
    private static BaseEvent getEvent(Class<?> subscribeCls, String methodName) {
        try{
            String key = subscribeCls.getSimpleName()+"_" +methodName;
            return (BaseEvent) eventBuilder.getEvent(key).newInstance();
        }catch(Exception e){
            Log.i(TAG,e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static interface BindEventBuilder{
        public BaseEventBuilder getEventBuilder();
    }
}
