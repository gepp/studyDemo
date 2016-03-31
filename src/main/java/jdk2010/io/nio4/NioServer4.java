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

    private static  Selector select;

    public static void init() throws IOException {
        select = Selector.open();
    }
    
    public static synchronized Set<SelectionKey> getSelectionKey(){
        return select.selectedKeys();
    }

    public static void main(String[] args) throws IOException {
        init();

        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress("localhost", 9999));
        channel.configureBlocking(false);

        channel.register(select, SelectionKey.OP_ACCEPT);
        System.out.println(Thread.currentThread().getName() + "服务器启动===================");
        
        final Executor executor=Executors.newFixedThreadPool(5);
        
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int keys = 0;
                    try {
                        keys = select.select();
                        if (keys > 0) {
                            Set<SelectionKey> selectionKeys = getSelectionKey();
                            Iterator<SelectionKey> iterator = selectionKeys.iterator();
                            while (iterator.hasNext()) {
                                SelectionKey key = iterator.next();
                                iterator.remove();
                                if (key.isAcceptable()) {
                                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                                    SocketChannel clientChannel;
                                    try {
                                        clientChannel = server.accept();
                                        clientChannel.configureBlocking(false);
                                        clientChannel.register(key.selector(), SelectionKey.OP_READ);
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                } else   {
                                    executor.execute(new HandlerRunnable(key));
                                    //SelectionKey.cancel只对下一次的Selector.cancel()有效
                                    key.cancel(); 
                                }  
                                    
                               
                            }
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
