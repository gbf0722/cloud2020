package com.atguigu;

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache cache = new MyCache();

        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(() -> {
                cache.put(temp + "", temp + "");
            }).start();
        }

        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(() -> {
                cache.get(temp + "");
            }).start();
        }
    }
}
