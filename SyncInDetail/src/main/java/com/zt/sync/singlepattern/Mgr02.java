package com.zt.sync.singlepattern;

/**
 * 代码示例2：和示例1类似，只不过将实例化在静态代码块中完成
 */
public class Mgr02 {
    public static void main(String[] args) {
        Mgr02 m1 = Mgr02.getInstance();
        Mgr02 m2 = Mgr02.getInstance();
        System.out.println(m1==m2);
    }
 
    //class文件加载时进行类的实例化
    private static Mgr02 INSTANCE ;
    static {
 
        INSTANCE = new Mgr02();
    }
 
    //构造方法私有化
    private Mgr02(){};
    public static Mgr02 getInstance(){return INSTANCE;}
    public void m(){ System.out.println("Mgr01"); }
 
}
