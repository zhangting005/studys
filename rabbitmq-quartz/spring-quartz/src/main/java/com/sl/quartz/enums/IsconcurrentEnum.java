package com.sl.quartz.enums;

public enum IsconcurrentEnum {

    CONCURRENT_IS("1"),  //支持并发
    CONCURRENT_NOT("0");  //不支持并发

    private String status;

    IsconcurrentEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
