package com.atguigu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Singleton01 {
    private static Singleton01 instance = null;
    private Singleton01() {
        System.out.println(Thread.currentThread().getName() + "  construction...");
    }
    public static Singleton01 getInstance() {
        if (instance == null) {
            instance = new Singleton01();
        }
        return instance;
    }

    //多线程环境下可能存在的安全问题
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(()-> Singleton01.getInstance());
        }
        executorService.shutdown();
    }
}
