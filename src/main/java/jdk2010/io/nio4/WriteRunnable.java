package jdk2010.io.nio4;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class WriteRunnable implements Runnable {
    SelectionKey key;
    String msg;

    public WriteRunnable(SelectionKey key, String msg) {
        this.key = key;
        this.msg = msg;
    }

    @Override
    public void run() {
        if (key.isValid()) {
            // ByteBuffer writebuffer = (ByteBuffer) key.attachment();
            // System.out.println(Thread.currentThread().getName() + "====½øÈëÐ´====");
            SocketChannel clientChannel = (SocketChannel) key.channel();
            try {
                System.out.println("msg´ý·¢ËÍ:"+msg);
                if (clientChannel.isOpen() && clientChannel != null) {
                    clientChannel.write(ByteBuffer.wrap(msg.getBytes()));
                }
                // key.cancel();
            } catch (ClosedChannelException e) {
                // System.out.println("readÊ§°Ü");
                e.printStackTrace();
                key.cancel();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // System.out.println("clientChannel.isConnected():"+clientChannel.isConnected());
            }
        }
    }
}
