package jdk2010.lock;

import java.util.concurrent.atomic.AtomicInteger;

public class TicketLock {

    private AtomicInteger serviceNum = new AtomicInteger();
    private AtomicInteger ticketNum = new AtomicInteger();

    public void lock() {
        ticketNum.getAndIncrement();
        while (ticketNum.get() != serviceNum.get()) {

        }
    }

    public void unlock(int myTicket) {
        int next = myTicket + 1;
        serviceNum.compareAndSet(myTicket, next);
    }
}
