package com.atguigu;

public class HelloGC {
    public static void main(String[] args) {
        System.out.println("hello GC...");
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

/*    public static void main(String[] args) {
        byte[] bytes = new byte[20 * 1024 * 1024];
    }*/
}