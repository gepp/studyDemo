package jdk2010.current.reentrant;

import java.util.concurrent.locks.ReentrantLock;

public class BufferInterruptibly {

    private ReentrantLock lock = new ReentrantLock();

    public void read() throws InterruptedException {
        lock.lockInterruptibly();// ע�����������Ӧ�ж�
        try {
            System.out.println("�����buff������");
        }catch (Exception e) {
            throw new InterruptedException("����read");
        } 
        
        finally {
            lock.unlock();
        }
    }

    public void write() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("��ʼ�����buffд�����ݡ�");
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
                    // ��5����ȥ�ж϶�
                    if (System.currentTimeMillis() - start > 5000) {
                        System.out.println("�����ˣ������ж�");
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
            System.out.println("�Ҳ�д��");
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
            buff.read();// �����յ��жϵ��쳣���Ӷ���Ч�˳�
        } catch (InterruptedException e) {
            System.out.println("�Ҳ�����");
        }
        System.out.println("������");
    }
}
