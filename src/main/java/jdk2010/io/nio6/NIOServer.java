package jdk2010.io.nio6;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class NIOServer {

    private Selector selector;
    private ServerSocketChannel ssc;

    private AtomicInteger clientTotal=new AtomicInteger(0);

    private void initServer(int port) throws IOException, ClosedChannelException {
        selector = Selector.open();
        ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void stopServer() {
        try {
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
            if (ssc != null && ssc.isOpen()) {
                ssc.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doAcceptable(SelectionKey key) throws IOException, ClosedChannelException {
        ServerSocketChannel tempSsc = (ServerSocketChannel) key.channel();
        SocketChannel ss = tempSsc.accept();
        ss.configureBlocking(false);
        ss.register(selector, SelectionKey.OP_READ);
        clientTotal.getAndIncrement();
        System.out.println("共："+clientTotal.intValue()+" 连接");
    }

    private void doWriteMessage(SelectionKey key) throws IOException, UnsupportedEncodingException {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.wrap(("您好" + key.attachment()).getBytes("UTF-8"));
        while (buffer.hasRemaining()) {
            sc.write(buffer);
        }
        sc.register(selector, SelectionKey.OP_READ);

    }

    private void doReadMessage(SelectionKey key) throws IOException, UnsupportedEncodingException {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println("receive from clint:");
        int read = 0;
        try {
            String from = "";
            while ((read = sc.read(buffer)) > 0) {
                buffer.flip();
                byte[] barr = new byte[buffer.remaining()];
                buffer.get(barr);
                from = from + new String(barr, "UTF-8");
                System.out.print(new String(barr, "UTF-8"));
                buffer.clear();
            }
            System.out.println("");
            sc.register(selector, SelectionKey.OP_WRITE, from);
        } catch (Exception e) {
            clientTotal.getAndDecrement();
            key.cancel();
            if (sc.isOpen()) {
                sc.close();
            }
            
            System.out.println("closeing ....剩余："+clientTotal.intValue()+"");
        }

    }

    public static void main(String[] args) throws IOException {
        int port = 7889;
        NIOServer server = new NIOServer();
        server.initServer(port);
        while (true) {
            // timeout:为正，则在等待某个通道准备就绪时最多阻塞 timeout 毫秒；如果为零，则无限期地阻塞；必须为非负数
            int nKeys = server.selector.select(1000);
            if (nKeys > 0) {

                Iterator<SelectionKey> iterator = server.selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        server.doAcceptable(key);
                    }
                    if (key.isWritable()) {
                        server.doWriteMessage(key);
                    }
                    if (key.isReadable()) {
                        server.doReadMessage(key);
                    }
                    iterator.remove();
                }

            }
        }

    }
}