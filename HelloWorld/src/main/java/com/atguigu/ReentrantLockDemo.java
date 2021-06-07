package com.atguigu;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    private Lock lock=new ReentrantLock();

    private void print() {
        lock.lock();
        doAdd();
        lock.unlock();
    }

    private void doAdd() {
        lock.lock();
        lock.lock();
        System.out.println("doAdd...");
        lock.unlock();
        lock.unlock();
    }

    public static void main(String[] args) {
        ReentrantLockDemo reentrantLockDemo = new ReentrantLockDemo();
        reentrantLockDemo.print();
    }
}
