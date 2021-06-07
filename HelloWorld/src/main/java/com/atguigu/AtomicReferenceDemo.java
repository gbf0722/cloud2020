package com.atguigu;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {
    public static void main(String[] args) {
        User cuzz = new User("cuzz", 18);
        User faker = new User("faker", 20);
        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(cuzz);
        System.out.println(atomicReference.compareAndSet(cuzz, faker)); // true
        System.out.println(atomicReference.get()); // User(userName=faker, age=20)
    }
}
