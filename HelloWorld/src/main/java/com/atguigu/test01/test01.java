package com.atguigu.test01;

public class test01 {
    static class Counter {
        private  int count = 0;

        public int getNext() {
            return ++count;
        }
    }

    public static void main(String[] args) {

        Counter counter = new Counter();

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    counter.getNext();
                }
            }).start();
        }

        System.out.println(counter.count);
    }


}

