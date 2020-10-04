package com.zt.sync.volatiles.sinleDCL;

/**
 * 我们也可以通过对齐填充，而避免伪共享。
 * 缓存行 通常都是 64 bit。而 long 为 8 个 bit，我们自己补充 7 个没有用 long 变量就可以让 x 和 7个没用的变量单独一个缓存行
 */
public class VolatileDemo6 {
    private static volatile Demo[] demos = new Demo[2];
    private static final class Demo {
        private volatile long x = 0L;
        // 缓存行对齐填充的无用数据
        private volatile long pading1, pading2, pading3, pading4, pading5, pading6, pading7;
    }
    static {
        demos[0] = new Demo();
        demos[1] = new Demo();
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            for (long i = 0; i < 10000_0000L; i++) {
                demos[0].x = i;
            }
        });
        Thread thread = new Thread(() -> {
            for (long i = 0; i < 10000_0000L; i++) {
                demos[1].x = i;
            }
        });
        long start = System.nanoTime();
        thread.start();
        thread1.start();
        thread.join();
        thread1.join();
        long end = System.nanoTime();
        long runSecond = (end - start) / 100_0000;
        System.out.println("运行毫秒:" + runSecond);
    }


}