package jdk2010.io.nio4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Nio4Client {

    private static Selector mSelector;

    private static SocketChannel mSocketChannel;

    private static ByteBuffer mReceiveBuf;

    private InetSocketAddress mRemoteAddress;

    public Nio4Client(InetSocketAddress remoteAddress) throws IOException {
        mSelector = Selector.open();
        this.mRemoteAddress = mRemoteAddress;

        mSocketChannel = SocketChannel.open();
        // �󶨵����ض˿�
        mSocketChannel.socket().setSoTimeout(30000);
        mSocketChannel.configureBlocking(false);
        if (mSocketChannel.connect(mRemoteAddress)) {
            System.out.println("��ʼ��������:" + mRemoteAddress);
        }
        mSocketChannel
                .register(mSelector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE, this);
        System.out.println(Thread.currentThread().getName() + "�˿ڴ򿪳ɹ�");
    }

    public void select() throws IOException {
        int nums = 0;
        nums = mSelector.select(100);
        if (nums > 0) {
            Iterator<SelectionKey> iterator = mSelector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                final SelectionKey key = iterator.next();
                iterator.remove();
                // ������Ƿ���Ч
                if (!key.isValid()) {
                    continue;
                }
                try {
                    if (key.isConnectable()) {
                        connect();
                    } else if (key.isReadable()) {
                        read(key);
                    }else if (key.isWritable()) {
                        write(key);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    key.cancel();
                }
            }
        }
    }

    public void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        Nio4Client client = new Nio4Client(new InetSocketAddress("localhost", 9999));
        client.select();

    }

    private boolean isConnected() {
        return mSocketChannel != null && mSocketChannel.isConnected();
    }

    private void read(SelectionKey key) throws IOException {
        // ������Ϣ
        final byte[] msg = recieve();
        if (msg != null) {
            String tmp = new String(msg);
            System.out.println(Thread.currentThread().getName() + "�������ݣ�" + tmp);
        }
    }
    
    private void write(SelectionKey key) throws IOException {
        new Thread(new NioClientRunnable(key)).start();
    }

    private byte[] recieve() throws IOException {
        if (isConnected()) {
            int len = 0;
            int readBytes = 0;

            synchronized (mReceiveBuf) {
                mReceiveBuf.clear();
                try {
                    while ((len = mSocketChannel.read(mReceiveBuf)) > 0) {
                        readBytes += len;
                    }
                } finally {
                    mReceiveBuf.flip();
                }
                if (readBytes > 0) {
                    final byte[] tmp = new byte[readBytes];
                    mReceiveBuf.get(tmp);
                    return tmp;
                } else {
                    System.out.println("���յ�����Ϊ��,������������");
                    return null;
                }
            }
        } else {
            System.out.println("�˿�û������");
        }
        return null;
    }

    private void connect() throws IOException {
        if (isConnected()) {
            return;
        }
        // ���SocketChannel������
        mSocketChannel.finishConnect();
        while (!mSocketChannel.isConnected()) {
            try {
                Thread.sleep(300);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
            mSocketChannel.finishConnect();
        }

    }
}
