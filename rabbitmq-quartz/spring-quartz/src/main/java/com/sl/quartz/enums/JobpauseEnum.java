package com.sl.quartz.enums;

public enum JobpauseEnum {

    STATUS_REUSME("1"),  //停止了
    STATUS_PAUSE("0");  //恢复了

    private String status;

    JobpauseEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
