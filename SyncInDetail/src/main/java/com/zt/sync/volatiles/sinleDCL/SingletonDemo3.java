package com.zt.sync.volatiles.sinleDCL;

/**
 * DCL 实现单例
 * 以上单例的实现看着没啥技术含量，下面介绍一下 DCL (Double-checked locking)，双重检查锁的实现，这也是面试会问到的点。
 */
public class SingletonDemo3 {
    // 考点在这里，要不要加 volitale
    private volatile static SingletonDemo3 INSTANCE;
    private SingletonDemo3() {

    }
    public static SingletonDemo3 getInstance() {
        if (INSTANCE == null) {
            synchronized (SingletonDemo3.class) {
                if (INSTANCE == null) {
                    // 对象实例化
                    INSTANCE = new SingletonDemo3();
                }
            }
        }
        return INSTANCE;
    }
} 
/**
 * 对象实例化实际可以分为几个步骤：
   1、分配对象空间
   2、初始化对象
   3、将对象指向分配的内存空间
          当指令重排的时候，2 和 3 会进行重排序，导致有的线程可能拿到未初始化的对象调用，存在风险问题。
*/

/**
    什么是指令重排？
    指令重排序是JVM为了优化指令，提高程序运行效率。指令重排序包括编译器重排序和运行时重排序。JVM规范规定，指令重排序可以在不影响单线程程序执行结果前提下进行。
    既然这样，那么在应用真正运行时可能是这个样子的：
    1.分配内存空间
    2.将内存空间的地址赋值给对应的引用
    3.初始化对象
*/