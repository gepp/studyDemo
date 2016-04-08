package jdk2010.io.nio4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sun.xml.internal.ws.util.StringUtils;

public class NioServer4 {

    private static Selector select;

    private static ByteBuffer sendBuf;

    static String msg = "";

    static ServerSocketChannel channel;

    public static synchronized Selector getSelect() {
        return select;
    }

    public static void init() throws IOException {
        int port = 9999;
        sendBuf = ByteBuffer.allocateDirect(1024);
        select = Selector.open();
        channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress("localhost", port));
        channel.configureBlocking(false);
        channel.register(select, SelectionKey.OP_ACCEPT);

        System.out.println(Thread.currentThread().getName() + "端口：" + port + "服务器启动===================");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        InputStreamReader input = new InputStreamReader(System.in);
                        BufferedReader br = new BufferedReader(input);
                        String sendText = br.readLine();
                        if (sendText != null && !sendText.equals("")) {
                            System.out.println("开始推送消息了");
                            msg = sendText;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        init();
        Thread.sleep(1000);
        final Executor executor = Executors.newFixedThreadPool(5);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Integer keys = 0;
                    try {
                        keys = select.select(1);
                        if (keys > 0) {
                            for (SelectionKey key : select.selectedKeys()) {
                                if (!key.isValid()) {
                                    continue;
                                }
                                try {
                                    if (key.isAcceptable()) {
                                        // executor.execute(new AcceptRunable(key));
                                        new AcceptRunable(key).run();
                                        // new Thread(new AcceptRunable(key)).start();
                                    } else if (key.isReadable()) {
                                        executor.execute(new ReadAndWriteRunnable(key));
                                        // key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
                                        // executor.execute(new WriteRunnable(key));
                                    } else if (key.isWritable()) {
                                        System.out.println("write");
                                        executor.execute(new WriteRunnable(key, msg));
                                    } else {
                                        System.out.println("else");
                                    }
                                } catch (Exception e) {
                                    key.cancel();
                                }
                                // key.cancel();
                                // select.wakeup();
                            }
                            select.selectedKeys().clear();
                        }
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
