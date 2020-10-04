package com.zt.sync.volatiles.see;

/**
 * 打印 修改 a=1 之后 程序停止。
 * 这是因为 volatile 标记的变量 a，主线程修改之后，并同步回主存，当其他的线程再使用变量 a 的时候，java 内存模型会让线程从主存加载变量 a。
 * 这就是 volatile 的 可见性 特点。
 */
public class VolatileDemo02 {
    // 代码的区别只是加了 volatile
    private static volatile int a = 0;
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (a == 0) {

            }
        }, "线程 1").start();
        System.out.println("修改 a=1 之前");
        Thread.sleep(3000);
        a = 1;
        System.out.println("修改 a=1 之后");
    }
}
