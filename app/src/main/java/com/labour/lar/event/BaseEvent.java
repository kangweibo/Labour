package com.labour.lar.event;

/**
 * Created by lx on 2017/6/9.
 */
public class BaseEvent {
    protected int code;
    protected String msg;
    protected Object data;
    protected int position;
    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
