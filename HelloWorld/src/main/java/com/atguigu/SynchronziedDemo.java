package com.atguigu;

public class SynchronziedDemo {

    private synchronized void print() {
        doAdd();
    }
    private synchronized void doAdd() {
        System.out.println("doAdd...");
    }

    public static void main(String[] args) {
        SynchronziedDemo synchronziedDemo = new SynchronziedDemo();
        synchronziedDemo.print(); // doAdd...
    }
}
