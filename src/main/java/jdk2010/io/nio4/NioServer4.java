package jdk2010.io.nio4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NioServer4 {

    private static Selector select;

    private static ByteBuffer sendBuf;

    public static synchronized Selector getSelect() {
        return select;
    }

    public static void init() throws IOException {
        sendBuf = ByteBuffer.allocateDirect(1024);
        select = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress("localhost", 9999));
        channel.configureBlocking(false);
        channel.register(select, SelectionKey.OP_ACCEPT);
        System.out.println(Thread.currentThread().getName() + "·þÎñÆ÷Æô¶¯===================");
    }

    public static void main(String[] args) throws IOException {
        init();

        final Executor executor = Executors.newFixedThreadPool(5);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Integer keys = 0;
                    try {
                        keys = select.select(1);
                        if (keys > 0) {
                            Set<SelectionKey> selectionKeys = select.selectedKeys();
                            Iterator<SelectionKey> iterator = selectionKeys.iterator();
                            while (iterator.hasNext()) {
                                SelectionKey key = iterator.next();
                                iterator.remove();
                                if (!key.isValid()) {
                                    continue;
                                }
                                try{
                                    if (key.isAcceptable()) {
//                                         executor.execute(new AcceptRunable(key));
                                        new AcceptRunable(key).run();
                                        //new Thread(new AcceptRunable(key)).start();
                                    } else if (key.isReadable()) {
                                        executor.execute(new ReadRunnable(key));
                                        // executor.execute(new WriteRunnable(key));
                                    } else if (key.isWritable()) {
                                        executor.execute(new WriteRunnable(key));
                                    }
                                }catch (Exception e) {
                                    key.cancel();
                                }
                                // key.cancel();
                                // select.wakeup();
                            }
                        }
                    } catch (IOException e) {
                        
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
