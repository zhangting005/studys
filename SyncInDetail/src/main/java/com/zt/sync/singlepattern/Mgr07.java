package com.zt.sync.singlepattern;

/**
 * 代码示例7：静态内部类
 * 说明：加载外部类时不会加载内部类，这样可以实现懒加载，JVM保证线程安全，类只加载一次
 */
public class Mgr07 {
    public static void main(String[] args) {
        for(int i=0;i<50;i++) {
            new Thread(()->{
                System.out.println(Mgr07.getInstance().hashCode());
            }).start();
        }
    }
    private static  class Mgr07Holder{
        private final static Mgr07 INSTANCE = new Mgr07();
    }
    private Mgr07(){};
    public static Mgr07 getInstance(){
        return Mgr07Holder.INSTANCE;
    }
    public void m(){ System.out.println("Mgr01"); }
}
