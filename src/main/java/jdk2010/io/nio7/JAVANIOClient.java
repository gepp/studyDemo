package jdk2010.io.nio7;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
 
public class JAVANIOClient {
    private SocketChannel client;
    private Selector selector = getSelector();
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(20));
 
    private volatile boolean isClose = false;
 
    private AtomicLong writeCount = new AtomicLong(0);
    private AtomicLong readCount = new AtomicLong(0);
 
    private AtomicBoolean isWriting = new AtomicBoolean(false);
    private AtomicBoolean isReading = new AtomicBoolean(false);
 
    public Selector getSelector() {
        try {
            return Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    public JAVANIOClient() {
        try {
            client = SocketChannel.open();
            client.configureBlocking(false);
            client.connect(new InetSocketAddress("localhost", 5555));
            client.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            System.out.println("�����ͻ��������쳣Client2" + e.getMessage());
            close();
        }
 
    }
 
    public void start() {
        threadPool.execute(new SelectorGuardHandler());
    }
 
    private class SelectorGuardHandler implements Runnable {
 
        @Override
        public void run() {
 
            while (!isClose) {
                try {
                    if (selector.select(10) == 0) {
                        continue;
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            if (isReading.get()) {
                                Thread.sleep(5);
                            } else {
                                isReading.set(true);
                                threadPool.execute(new ReceiveMessageHandler(socketChannel));
                            }
 
                        } else if (selectionKey.isWritable()) {
                            Object requestMessage = selectionKey.attachment();
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            selectionKey.interestOps(SelectionKey.OP_READ);
                            threadPool.execute(new SendMessageHandler(socketChannel, requestMessage));
                        } else if (selectionKey.isConnectable()) {
                            SocketChannel sc = (SocketChannel) selectionKey.channel();
                            sc.finishConnect();
                            sc.register(selector, SelectionKey.OP_READ);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("�ͻ��������������쳣[start]��" + e.getMessage());
                    close();
                }
            }
        }
    }
 
    /**
     * ���������߳�
     * 
     * @author haoguo
     * 
     */
    private class SendMessageHandler implements Runnable {
        private SocketChannel client;
 
        private Object requestMessage;
 
        private SendMessageHandler(SocketChannel client, Object requestMessage) {
            this.client = client;
            this.requestMessage = requestMessage;
        }
 
        @Override
        public void run() {
            try {
                byte[] requestMessageByteData = null;
                if (requestMessage instanceof byte[]) {
                    requestMessageByteData = (byte[]) requestMessage;
                } else if (requestMessage instanceof String) {
                    requestMessageByteData = ((String) requestMessage).getBytes();
                }
                if (requestMessageByteData == null || requestMessageByteData.length == 0) {
                    System.out.println("�ͻ��˷��͵�����Ϊ��");
                } else {
 
                    ByteBuffer data = ByteBuffer.allocate(requestMessageByteData.length + 4);
                    data.putInt(requestMessageByteData.length);
                    data.put(requestMessageByteData);
                    data.flip();
                    while (data.hasRemaining()) {
                        client.write(data);
                    }
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                    System.out.println("[" + sdf.format(date) + "][" + Thread.currentThread().getId() + "]�ͻ��˷������ݣ�["
                            + new String(requestMessageByteData) + "]");
                }
            } catch (Exception e) {
                System.out.println("�ͻ��˷�������ʧ�ܣ�[" + e.getMessage() + "]");
                close();
            } finally {
                isWriting.set(false);
                writeCount.decrementAndGet();
            }
 
        }
    }
 
    /**
     * �������߳�
     * 
     * @author haoguo
     * 
     */
    private class ReceiveMessageHandler implements Runnable {
        private SocketChannel client;
        private ByteBuffer dataLen = ByteBuffer.allocate(4);
 
        private ReceiveMessageHandler(SocketChannel client) {
            this.client = client;
        }
 
        @Override
        public void run() {
            try {
                dataLen.clear();
                int len = 4;
                while (len > 0) {
                    int readLen = client.read(dataLen);
                    if (readLen < 0) {
                        throw new Exception("readLen==" + readLen);
                    } else if (readLen == 0) {
                        System.out.println(Thread.currentThread().getId() + "readLen == 0");
                        return;
                    }
                    len -= readLen;
                }
                // dataLen.flip();
                // int data_length = dataLen.getInt();
                int data_length = dataLen.getInt(0);
                ByteBuffer data = ByteBuffer.allocate(data_length);
                while (data.hasRemaining()) {
                    client.read(data);
                }
 
                String receiveData = new String(data.array());
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                System.out.println("[" + sdf.format(date) + "][" + Thread.currentThread().getId() + "]�ͻ��˽��յ�������["
                        + client.getRemoteAddress() + "]���� :[" + receiveData + "]");
                readCount.incrementAndGet();
            } catch (Exception e) {
                System.out.println("�ͻ��˽�������ʧ�ܣ�" + e);
                close();
            } finally {
                isReading.set(false);
            }
        }
    }
 
    public boolean isClose() {
        return isClose;
    }
 
    public void setClose(boolean close) {
        this.isClose = close;
    }
 
    public void close() {
        try {
            threadPool.shutdown();
            isClose = true;
            if (selector != null) {
                selector.close();
            }
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            System.out.println("�رտͻ����쳣��" + e.getMessage());
        }
    }
 
    public void writeData(String data) {
        while (isWriting.get()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            isWriting.set(true);
            writeCount.incrementAndGet();
            client.register(selector, SelectionKey.OP_WRITE, data);
        } catch (Exception e) {
            System.out.println("�ͻ���ע��дͨ���쳣��" + e.getMessage());
        }
    }
 
    public boolean hasWriteTask() {
        return writeCount.get() != 0;
    }
 
    public long getRecive() {
        return readCount.get();
    }
 
    public static void main(String[] args) {
        for (int j = 0; j < 20; j++) {
            new Thread(new Runnable() {
 
                @Override
                public void run() {
 
                    final JAVANIOClient client = new JAVANIOClient();
                    client.start();
                    try {
                        TimeUnit.MILLISECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long t1 = System.currentTimeMillis();
                    for (int i = 0; i < 500; i++) {
                        String ii = "00" + i;
                        ii = ii.substring(ii.length() - 3);
                        client.writeData(ii
                                + "nimddddddddddsssssssssssssssssssssssssssssssssssscccccccccccccccccccccccc"
                                + "ccccccccccccccccccccccccccccccccccccccccccccccccccccccccdddddddddddd"
                                + "dddddddddddddddddwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwaaaaaaaaaaaaaa"
                                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaddddddddddddddddddddddddddddddd"
                                + "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddrrrr"
                                + "jjjjjjjjjjjjjjjjjjjjjjjjjjjjrrrrrrrrrrrrrrrrrrrrrrrrrrrkkkkkkkkkkkkkkkkkkkk"
                                + "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkjjjjkkkkkklllllllllllllllllllllllllll"
                                + "lllllllldddddddddddddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmddaei"
                                + "nimddddddddddsssssssssssssssssssssssssssssssssssscccccccccccccccccccccccc"
                                + "ccccccccccccccccccccccccccccccccccccccccccccccccccccccccdddddddddddd"
                                + "dddddddddddddddddwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwaaaaaaaaaaaaaa"
                                + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaddddddddddddddddddddddddddddddd"
                                + "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddrrrr"
                                + "jjjjjjjjjjjjjjjjjjjjjjjjjjjjrrrrrrrrrrrrrrrrrrrrrrrrrrrkkkkkkkkkkkkkkkkkkkk"
                                + "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkjjjjkkkkkklllllllllllllllllllllllllll"
                                + "lllllllldddddddddddddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmddaei" + i);
                    }
 
                    while (client.hasWriteTask()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
 
                            e.printStackTrace();
                        }
                    }
                    while (client.getRecive() != 500) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    long t2 = System.currentTimeMillis();
                    System.out.println("�ܹ���ʱ��" + (t2 - t1) + "ms");
                    client.close();
                }
 
            }).start();
        }
    }
}
