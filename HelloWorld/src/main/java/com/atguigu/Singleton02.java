package com.atguigu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Singleton02 {
    private static volatile Singleton02 instance = null;
    private Singleton02() {
        System.out.println(Thread.currentThread().getName() + "  construction...");
    }

    //双重锁单例  解决多线程单例安全问题  volatile和双重判断
    public static Singleton02 getInstance() {
        if (instance == null) {
            synchronized (Singleton01.class) {
                if (instance == null) {
                    instance = new Singleton02();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(()-> Singleton02.getInstance());
        }
        executorService.shutdown();
    }
}
