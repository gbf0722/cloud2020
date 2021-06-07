package com.atguigu;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContainerDemo {
    public static void main(String[] args) {
        //List<Integer> list = new ArrayList<>();
        //List<Integer> list =Collections.synchronizedList(new ArrayList<>());
        List<Integer> list = new CopyOnWriteArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                list.add(random.nextInt(10));
                System.out.println(list);
            }).start();
        }
    }
}
