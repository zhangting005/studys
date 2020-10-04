package com.zt.sync.volatiles.sinleDCL;

/**
 *懒汉式
 *饿汉式不管你用不用这个单例，只要类加载，单例就给你初始化好了。有的人就想让其懒加载，节约那可怜的内存，用的时候单例再实例化。
 *运行的时候加上这个 -XX:+TraceClassLoading 会打印加载的类。
 *我们可以看到调用 SingletonDemo1.getInstance() 的时候，才加载的 SingletonDemo1Holder 类，再实例化单例，达到懒加载的要求。
 */
public class SingletonDemo2 {
    private SingletonDemo2() {
    }

    public static SingletonDemo2 getInstance() {
        System.out.println("SingletonDemo1Holder 类加载");
        return SingletonDemo2Holder.getInstance();
    }

    private static class SingletonDemo2Holder {
        private static final SingletonDemo2 INSTANCE = new SingletonDemo2();
        public static SingletonDemo2 getInstance() {
            return SingletonDemo2Holder.INSTANCE;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(SingletonDemo2.getInstance());
        System.out.println(SingletonDemo2.getInstance());
    }
}
