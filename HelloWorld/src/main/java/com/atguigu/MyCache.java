package com.atguigu;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyCache {

    private volatile Map<String, Object> map = new HashMap<>();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

    public void put(String key, Object value) {
        try {
            writeLock.lock();
            System.out.println(Thread.currentThread().getName() + " 正在写入...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + " 写入完成，写入结果是 " + value);
        } finally {
            writeLock.unlock();
        }
    }

    public void get(String key) {
        try {
            readLock.lock();
            System.out.println(Thread.currentThread().getName() + " 正在读...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Object res = map.get(key);
            System.out.println(Thread.currentThread().getName() + " 读取完成，读取结果是 " + res);
        } finally {
            readLock.unlock();
        }
    }
}
