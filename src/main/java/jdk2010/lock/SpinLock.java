package jdk2010.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * зда§Ыј
 * 
 * @author 15060195
 * 
 */
public class SpinLock {
    private AtomicReference<Thread> owner = new AtomicReference<Thread>();

    public void lock() {
        Thread currentThread = Thread.currentThread();
        while (!owner.compareAndSet(null, currentThread)) {

        }
    }

    public void unlock() {
        owner.compareAndSet(Thread.currentThread(), null);
    }
}
