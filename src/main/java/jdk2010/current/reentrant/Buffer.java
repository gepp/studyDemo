package jdk2010.current.reentrant;

public class Buffer {

    public synchronized void read() {
        System.out.println("read");
    }

    public synchronized void write() {
        long startTime = System.currentTimeMillis();
        System.out.println("开始往这个buff写入数据…");
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
                    // 等5秒钟去中断读
                    while (System.currentTimeMillis() - start > 5000) {
                        System.out.println("不等了，尝试中断");
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
        buff.read();// 这里估计会一直阻塞
        System.out.println("读结束");
    }
}
