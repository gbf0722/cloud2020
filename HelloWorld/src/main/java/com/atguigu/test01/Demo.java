package com.atguigu.test01;



/**
 * 线程 操作 资源类
 * @author Administrator
 *
 */
public class Demo {

    public static void main(String[] args) {

        Ticket ticket = new Ticket();


        Thread t1 = new Thread(ticket, "窗口一");
        Thread t2 = new Thread(ticket, "窗口二");
        Thread t3 = new Thread(ticket, "窗口三");
        t1.start();
        t2.start();
        t3.start();
        }



}
