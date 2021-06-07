package com.atguigu.single;

/**
 * 懒汉式：
 * 延迟构造这个实例对象
 * <p>
 * 1.构造器私有化
 * 2.用一个静态变量保存这个唯一的实例
 * 3.提供一个静态方法，获取这个实例对象
 */
public class Singleton3 {
    private static Singleton3 instance;

    private Singleton3() {

    }

    public static Singleton3 getInstance() {
        if (instance == null) {
            synchronized (Singleton3.class) {
                if (instance == null) {
                    try {
                        Thread.sleep(100);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    instance = new Singleton3();
                }

            }
        }
        return instance;
    }

}





