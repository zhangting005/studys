package com.sl.quartz.enums;

public enum TableQueueStatusEnum {

    NOT_SEND("NOT_SEND"),  //还没有发送
    HAS_SEND("HAS_SEND"),  //已经发送了
    NOT_HANDLE("NOT_HANDLE"), //还没有处理
    SUCCESS_HANDLE("HAS_HANDLE"),  //已经成功
    FAIL_HANDLE("FAIL_HANDLE");  //处理失败

    private String status;

    TableQueueStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
