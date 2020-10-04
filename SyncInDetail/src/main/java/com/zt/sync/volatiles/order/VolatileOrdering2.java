package com.zt.sync.volatiles.order;

/**
 * java 中的字节码最终都会编译成机器码（CPU 指令）执行，CPU 在保证单线程中执行结果不变的情况下，可以对指令进行指令重排已达到提高执行效率。
 */
public class VolatileOrdering2 {
    static  int b = 1;
    public static void main(String[] args) throws InterruptedException {
        int a = 0;
        b = 2;
        a += 1;
        System.out.println(a);
    }
}

/**
 * 上述代码指令重排执行顺序的可能：
 * int a=0;
 * a+=1;
 * System.out.println(a);
 * int b = 2;
*/