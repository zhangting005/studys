package com.sl.quartz.entity;

import java.io.Serializable;

public class RetObj implements Serializable {

    private static final long serialVersionUID = -6410748379024140027L;
    private boolean flag = true;
    private String msg;
    private Object obj;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }


}
