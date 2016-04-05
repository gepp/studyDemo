package jdk2010.io.nio4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;

public class NioClientRunnable implements Runnable {
    SelectionKey key;

    public NioClientRunnable(SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {
        if (key.isValid()) {
            // System.out.println(Thread.currentThread().getName() + "====ฝ๘ศ๋ะด====");
            ByteBuffer buffer = ByteBuffer.wrap("say to server".getBytes());
            SocketChannel clientChannel = (SocketChannel) key.channel();
            try {
                if (clientChannel.isOpen() && clientChannel != null) {
                    buffer.clear();
                    clientChannel.write(buffer);
                    buffer.flip();
                    clientChannel.close();
                }
            } catch (ClosedChannelException e) {
                key.cancel();
            } catch (IOException e) {
            }
        }
    }
}
