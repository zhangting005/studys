package com.zt.thread.reference;

import java.io.IOException;

/**
 * 强：只要有引用就不会回收
 */
public class T01_NormalReference {
    public static void main(String[] args) throws IOException {
        M m = new M();
        m = null;
        System.gc(); //DisableExplicitGC

        System.in.read();
    }
}
