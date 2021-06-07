package com.atguigu.test01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 需求:3个窗口卖40张票
 * 线程                  操作                              资源类
 * 3个线程        sale()        票
 * @author Administrator
 *
 */
public class Ticket implements Runnable {

    private int tickets = 40;
    // 定义锁
    private Lock lock = new ReentrantLock();

    public void run() {
        while (tickets > 0) {
            // 加锁
            lock.lock();
            if (tickets > 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "售出了第" + (tickets--) + "张票");
            }
            lock.unlock();
        }
    }
}

