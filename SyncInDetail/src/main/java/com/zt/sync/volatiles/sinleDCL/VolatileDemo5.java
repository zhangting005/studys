package com.zt.sync.volatiles.sinleDCL;
/**
 * 下述代码，使用 @sun.misc.Contended 避免伪共享，我电脑运行 运行毫秒:813
 */
//运行的时候，需要加上参数 -XX:-RestrictContended
public class VolatileDemo5 {
 private static volatile Demo[] demos = new Demo[2];
 @sun.misc.Contended
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
/**
 * 上述代码展示了伪共享会降低代码的运行速度。什么是伪共享呢。
     还记得 Cpu 中的 L1 L2 L3 吗，主存中的数据加载到 Cpu 的高速缓存的最小单位就是 缓存行（64 bit）。Cpu 的缓存失效，也是以缓存行为单位失效。
     当 Cpu 从内存加载数据的时候，它会把可能会用到的数据和目标数据一起加载到 L1/L2/L3 中。
     上述代码的变量 private static volatile Demo[] demos = new Demo[2]; 
     这两个变量被一起加载到同一个缓存行中去了，一个线程修改了其中的 demos[0].x 导致缓存行失效，
     另一个线程修改 demos[1].x = i; 的时候发现缓存行失效，会去主存重新加载新的数据，两个线程相互影响导致不停从内存加载，运行速度自然降低了。
   @sun.misc.Contended 作用就是让其单独在一个缓存行中去。
*/