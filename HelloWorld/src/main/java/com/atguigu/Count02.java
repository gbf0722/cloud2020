package com.atguigu;

public class Count02 {
    NotReentrantLock lock = new NotReentrantLock();
    public void print() throws InterruptedException{
        lock.lock();
        doAdd();
        lock.unlock();
    }

    private void doAdd() throws InterruptedException {
        lock.lock();
        // do something
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        Count02 count = new Count02();
        count.print();
    }
}
