package com.zt.sync.volatiles.sinleDCL;

/**
 * 伪共享
 * volatile 给我们带来了变量 可见性 的功能，但是当使用不当，会掉入另一个 伪共享 的坑。先看 demo.
 * 下面述代码，存在伪共享的情况，我电脑运行 运行毫秒:2764
 */
public class VolatileDemo4 {
    private static volatile Demo[] demos = new Demo[2];
//    @sun.misc.Contended
    private static final class Demo {
        private volatile long x = 0L;
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