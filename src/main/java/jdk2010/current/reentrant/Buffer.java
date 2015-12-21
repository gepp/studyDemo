package jdk2010.current.reentrant;

public class Buffer {

    public synchronized void read() {
        System.out.println("read");
    }

    public synchronized void write() {
        long startTime = System.currentTimeMillis();
        System.out.println("��ʼ�����buffд�����ݡ�");
        for (;;) {
            if (System.currentTimeMillis() - startTime > Integer.MAX_VALUE) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        Buffer buff = new Buffer();
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
                    while (System.currentTimeMillis() - start > 5000) {
                        System.out.println("�����ˣ������ж�");
                        reader.interrupt();
                        break;
                    }
                }
            }
        }).start();
    }
}

class Writer extends Thread {
    private Buffer buff;

    public Writer(Buffer buff) {
        this.buff = buff;
    }

    @Override
    public void run() {
        buff.write();
    }
}

class Reader extends Thread {
    private Buffer buff;

    public Reader(Buffer buff) {
        this.buff = buff;
    }

    @Override
    public void run() {
        buff.read();// ������ƻ�һֱ����
        System.out.println("������");
    }
}
