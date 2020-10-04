package com.zt.sync.volatiles.see;

/**
 * 运行这个程序，代码会一直运行，不会停止。这是因为 线程1 的工作内存 a 为 0，而主线程尽管修改了 a，但不会达到线程1重新加载主存中的变量 a。
 *
 */
public class VolatileDemo01 {
    private static  int a = 0;
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
