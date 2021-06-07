package com.atguigu;

public class ReSortSeqDemo {
    int a = 0;
    boolean flag = false;

    public void method01() {
        a = 1;           // flag = true;
        // ----线程切换----
        flag = true;     // a = 1;
    }

    public void method02() {
        if (flag) {
            a = a + 3;
            System.out.println("a = " + a);
        }
    }


}
