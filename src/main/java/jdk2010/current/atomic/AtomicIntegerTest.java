package jdk2010.current.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerTest {
    public static void main(String[] args) {
        AtomicInteger integer=new AtomicInteger(10);
        integer.compareAndSet(90,15);
        System.out.println(integer.get());
    }
}
