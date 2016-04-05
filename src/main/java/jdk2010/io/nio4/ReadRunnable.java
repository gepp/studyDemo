package jdk2010.io.nio4;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ReadRunnable implements Runnable {
    SelectionKey key;

    public ReadRunnable(SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {
        if (key.isValid()) {
            //System.out.println(Thread.currentThread().getName() + "====Ω¯»Î∂¡====");
            SocketChannel clientChannel = (SocketChannel) key.channel();
            if (clientChannel.isConnected() && clientChannel != null) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int length = 0;
                try {
                    while ((length = clientChannel.read(buffer)) != -1) {
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, length));
                        buffer.clear();
                    }
                    clientChannel.register(NioServer4.getSelect(), SelectionKey.OP_WRITE);
                }
                catch (ClosedChannelException e ) {
                    //System.out.println("read ß∞‹");
                    //e.printStackTrace();
                    key.cancel();
                }
                catch (IOException e) {
                    // TODO: handle exception
                }
            }
        }
    }
}
