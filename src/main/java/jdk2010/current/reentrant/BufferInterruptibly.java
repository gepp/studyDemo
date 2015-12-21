package jdk2010.current.reentrant;

import java.util.concurrent.locks.ReentrantLock;

public class BufferInterruptibly {

    private ReentrantLock lock = new ReentrantLock();

    public void read() throws InterruptedException {
        lock.lockInterruptibly();// 注意这里，可以响应中断
        try {
            System.out.println("从这个buff读数据");
        }catch (Exception e) {
            throw new InterruptedException("放弃read");
        } 
        
        finally {
            lock.unlock();
        }
    }

    public void write() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("开始往这个buff写入数据…");
            for (;;) {
                if (System.currentTimeMillis() - startTime > Integer.MAX_VALUE) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        BufferInterruptibly buff = new BufferInterruptibly();
        final Writer writer = new Writer(buff);
        final Reader reader = new Reader(buff);
        writer.start();
        reader.start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                long start = System.currentTimeMillis();
                for (;;) {
                    // 等5秒钟去中断读
                    if (System.currentTimeMillis() - start > 5000) {
                        System.out.println("不等了，尝试中断");
                        writer.interrupt();
                        break;
                    }
                }
            }
        }).start();
    }
}

class Writer extends Thread {
    private BufferInterruptibly buff;

    public Writer(BufferInterruptibly buff) {
        this.buff = buff;
    }

    @Override
    public void run() {
        try {
            buff.write();
        } catch (InterruptedException e) {
            System.out.println("我不写了");
            e.printStackTrace();
        }
    }
}

class Reader extends Thread {
    private BufferInterruptibly buff;

    public Reader(BufferInterruptibly buff) {
        this.buff = buff;
    }

    @Override
    public void run() {
        try {
            buff.read();// 可以收到中断的异常，从而有效退出
        } catch (InterruptedException e) {
            System.out.println("我不读了");
        }
        System.out.println("读结束");
    }
}
