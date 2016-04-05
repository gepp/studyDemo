package jdk2010.io.nio5;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 消息推送服务器
 * 
 * @author zengjiantao
 * @date 2013-4-8
 */
public class PushServer extends Thread {

    private static final int BUFFER_SIZE = 1024;

    /**
     * 服务器连接通道
     */
    private ServerSocketChannel serverSocketChannel;

    /**
     * 发送缓冲区
     */
    private final ByteBuffer sendBuf;

    /**
     * 端口选择器
     */
    private Selector selector;

    /**
     * 服务器端口
     */
    private final int mPort;

    /**
     * 线程是否结束的标志
     */
    private final AtomicBoolean shutdown;

    /**
     * 发送消息的开关
     */
    private final AtomicBoolean sendable;

    /**
     * 发送消息的内容
     */
    private String sendMsg;

    private final ExecutorService executorService;

    public PushServer(int port) {
        mPort = port;
        // 初始化缓冲区
        sendBuf = ByteBuffer.allocateDirect(BUFFER_SIZE);
        if (selector == null) {
            // 创建新的Selector
            try {
                selector = Selector.open();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        startup();
        executorService = Executors.newFixedThreadPool(10);
        shutdown = new AtomicBoolean(false);
        sendable = new AtomicBoolean(false);
    }

    private void startup() {
        try {
            // 打开通道
            serverSocketChannel = ServerSocketChannel.open();
            // 绑定到本地端口
            serverSocketChannel.socket().setSoTimeout(30000);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(mPort));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器端口打开成功");

        } catch (final IOException e1) {
            e1.printStackTrace();
        }
    }

    private void select() {
        int nums = 0;
        try {
            if (selector == null) {
                return;
            }
            nums = selector.select(1000L);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // 如果select返回大于0，处理事件
        if (nums > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                // 得到下一个Key
                final SelectionKey key = iterator.next();
                iterator.remove();
                // 检查其是否还有效
                if (!key.isValid()) {
                    continue;
                }

                // 处理事件
                if (key.isAcceptable()) {
                    executorService.execute(new Accepter(key));
                    // accept(key);
                } else if (key.isWritable()) {
                    if (sendable.get()) {
                        executorService.execute(new Sender(key, sendMsg));
                    }
                }
            }
            if (sendable.get()) {
                System.out.println("结束推送消息了");
            }
            sendable.set(false);
        }
    }

    /**
     * 用于连接的Runnable
     * 
     * @author zengjiantao
     * @date 2013-4-11
     */
    class Accepter implements Runnable {

        private final SelectionKey key;

        public Accepter(SelectionKey key) {
            this.key = key;
        }

        @Override
        public void run() {
            accept(key);
        }

    }

    /**
     * 用于发送消息的Runnable
     * 
     * @author zengjiantao
     * @date 2013-4-11
     */
    class Sender implements Runnable {

        private final SelectionKey key;

        private final String msg;

        public Sender(SelectionKey key, String msg) {
            this.key = key;
            this.msg = msg;
        }

        @Override
        public void run() {
            send(key, msg);
        }

    }

    /**
     * 接收客户端
     * 
     * @param key
     * @throws IOException
     */
    private void accept(SelectionKey key) {
        // 打开通道
        try {
            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
            // 绑定到本地端口
            socketChannel.socket().setSoTimeout(30000);
            socketChannel.configureBlocking(false);
            synchronized (selector) {
                socketChannel.register(selector, SelectionKey.OP_WRITE);
            }
            System.out.println(Thread.currentThread().getName() + "端口打开成功");
        } catch (IOException e) {
            System.out.println("端口打开失败");
            e.printStackTrace();
            key.cancel();
        }
    }

    @Override
    public void run() {
        // 启动主循环流程
        while (!shutdown.get()) {
            try {
                select();
                try {
                    Thread.sleep(10);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        shutdown();
    }

    /**
     * 打开发送消息的开关
     * 
     * @param msg
     */
    private void send(final String msg) {
        sendMsg = msg;
        sendable.set(true);
        System.out.println("开始推送消息了");
    }

    /**
     * 向指定连接发送消息
     * 
     * @param key
     * @param msg
     */
    private void send(final SelectionKey key, final String msg) {
        try {
            byte[] out = msg.getBytes();
            if (out == null || out.length < 1) {
                return;
            }
            //synchronized (sendBuf) {
                sendBuf.clear();
                sendBuf.put(out);
                sendBuf.flip();
            //}
            SocketChannel socketChannel = (SocketChannel) key.channel();
            socketChannel.write(sendBuf);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开连接
     */
    public void disConnect() {
        shutdown.set(true);
    }

    /**
     * 关闭端口选择器
     */
    private void shutdown() {
        if (serverSocketChannel != null) {
            try {
                serverSocketChannel.close();
                while (serverSocketChannel.isOpen()) {
                    try {
                        Thread.sleep(300L);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                    serverSocketChannel.close();
                }
                System.out.println("端口关闭成功");
            } catch (IOException e1) {
                System.err.println("端口关闭错误:");
                e1.printStackTrace();
            } finally {
                serverSocketChannel = null;
            }
        }
        // 关闭端口选择器
        if (selector != null) {
            try {
                selector.close();
                System.out.println("端口选择器关闭成功");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                selector = null;
            }
        }
    }

    public static void main(String[] args) {
        try {
            final PushServer server = new PushServer(9999);
            server.start();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        try {
                            InputStreamReader input = new InputStreamReader(System.in);
                            BufferedReader br = new BufferedReader(input);
                            String sendText = br.readLine();
                            server.send(sendText);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
