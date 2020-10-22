package com.sl.quartz.enums;

public enum JobstatusEnum {

    STATUS_RUNNING("1"),  //正在运行
    STATUS_NOT_RUNNING("0");  //还没有运行

    private String status;

    JobstatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
