package com.atguigu;

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileDemo02 {
    public static void main(String[] args) {
        // test01();
        test02();
    }

    // 测试原子性
    private static void test02() {
        Data02 data = new Data02();
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    data.addOne();
                }
            }).start();
        }
        // 默认有 main 线程和 gc 线程
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        //System.out.println(data.a);

        System.out.println(data.atomicInteger);
    }
}

 class Data02 {
    volatile int a = 0;
    AtomicInteger atomicInteger=new AtomicInteger();
    
    void addOne() {
        //this.a += 1;
        atomicInteger.getAndIncrement();
    }
}
