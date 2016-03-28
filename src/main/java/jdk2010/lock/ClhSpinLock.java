package jdk2010.lock;

import java.util.concurrent.atomic.AtomicReference;

public class ClhSpinLock {
    private final ThreadLocal<Node> prev;
    private final ThreadLocal<Node> node;
    private final AtomicReference<Node> tail = new AtomicReference<Node>(new Node());

    public ClhSpinLock() {
        this.node = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return new Node();
            }
        };

        this.prev = new ThreadLocal<Node>() {
            protected Node initialValue() {
                return null;
            }
        };
    }

    public void lock() throws InterruptedException {
        final Node node = this.node.get();
        node.locked = true;
        Node pred = this.tail.getAndSet(node);
        this.prev.set(pred);
       // System.out.println("lock:"+pred.locked);
        while (pred.locked) {// 进入自旋
            System.out.println(Thread.currentThread().getId() + "自旋");
            Thread.sleep(1000);
        }
    }

    public void unlock() {
        final Node node = this.node.get();
        node.locked = false;
      //  System.out.println("this.prev.get():"+this.prev.get().locked);
        this.node.set(this.prev.get());
    }

    private static class Node {
        private volatile boolean locked=false;
    }
    public static void main(String[] args) throws InterruptedException {
//        final ClhSpinLock lock = new ClhSpinLock();
//        lock.lock();
//        lock.unlock();
//        lock.lock();
//        lock.lock();
//        
//        System.out.println("over");
        final ClhSpinLock lock = new ClhSpinLock();
        lock.lock();
        for (int i = 0; i < 10; i++) {
          new Thread(new Runnable() {
            @Override
            public void run() {
              try {
                lock.lock();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
              System.out.println(Thread.currentThread().getId() + " acquired the lock!");
              lock.unlock();
            }
          }).start();
          Thread.sleep(100);
        }
        System.out.println(Thread.currentThread().getId() + "main thread unlock!");
        lock.unlock();
    }
}
