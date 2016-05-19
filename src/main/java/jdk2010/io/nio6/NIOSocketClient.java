package jdk2010.io.nio6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NIOSocketClient extends Thread {
    private SocketChannel socketChannel;
    private Selector selector;

    /**
     * @param args
     */
    public static void main(String[] args) {
        NIOSocketClient client = null;
        try {
            for (int i = 0; i < 100; i++) {
                client = new NIOSocketClient();
                client.initClient();
                client.start();
            }
            // client.setDaemon(true);
        } catch (Exception e) {
            e.printStackTrace();
            client.stopServer();
        }
    }

    public void run() {
        while (true) {
            try {
                // 写消息到服务器端
                writeMessage();

                int select = selector.select();
                if (select > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = keys.iterator();
                    while (iter.hasNext()) {
                        SelectionKey sk = iter.next();
                        if (sk.isReadable()) {
                            readMessage(sk);
                        }
                        iter.remove();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void readMessage(SelectionKey sk) throws IOException, UnsupportedEncodingException {
        SocketChannel curSc = (SocketChannel) sk.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println("Receive from server:");
        int read = 0;
        while ((read = curSc.read(buffer)) > 0) {
            buffer.flip();
            byte[] barr = new byte[buffer.remaining()];
            buffer.get(barr);
            System.out.print(new String(barr, "UTF-8"));
            buffer.clear();
        }

        System.out.println();
    }

    public void writeMessage() throws IOException {
        try {
            socketChannel.write(ByteBuffer.wrap(("hello1hello1hello1hello1hello1hello1hello1hello1hello1hello1hello1hello1hello1hello1hello1hello1").getBytes("UTF-8")));
            socketChannel.write(ByteBuffer.wrap(("hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2hello2").getBytes("UTF-8")));
            // String ss = "Server,how are you?";
            // ByteBuffer buffer = ByteBuffer.wrap(ss.getBytes("UTF-8"));
            // while (buffer.hasRemaining()) {
            // // System.out.println("buffer.hasRemaining() is true.");
            // socketChannel.write(buffer);
            // }
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
            String command = systemIn.readLine();
            ByteBuffer buffer = ByteBuffer.wrap(command.getBytes("UTF-8"));
           // socketChannel.write(buffer);

        } catch (IOException e) {
            if (socketChannel.isOpen()) {
                socketChannel.close();
            }
            e.printStackTrace();
        }
    }

    public void initClient() throws IOException, ClosedChannelException {
        InetSocketAddress addr = new InetSocketAddress(7889);
        socketChannel = SocketChannel.open();
        selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        // 连接到server
        socketChannel.connect(addr);
        while (!socketChannel.finishConnect()) {
            System.out.println("check finish connection");
        }
    }

    /**
     * 停止客户端
     */
    private void stopServer() {
        try {
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
            if (socketChannel != null && socketChannel.isOpen()) {
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}