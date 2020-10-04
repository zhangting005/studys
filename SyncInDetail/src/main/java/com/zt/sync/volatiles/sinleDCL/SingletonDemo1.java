package com.zt.sync.volatiles.sinleDCL;

/**
 * 饿汉式
 * 一般项目中我们用这种用法即可，简单方便，也没谁闲着无聊利用别的手段给你打破单例。
 */
public class SingletonDemo1 {
    private static final SingletonDemo1 INSTANCE = new SingletonDemo1();
    private SingletonDemo1() {
    }
    public static SingletonDemo1 getInstance() {
        return SingletonDemo1.INSTANCE;
    }
}
