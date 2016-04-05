package jdk2010.io.nio4;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class WriteRunnable implements Runnable {
    SelectionKey key;

    public WriteRunnable(SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {
        if (key.isValid()) {
            //System.out.println(Thread.currentThread().getName() + "====Ω¯»Î–¥====");
            ByteBuffer buffer = ByteBuffer.wrap("return data aaaaaaa".getBytes());
            SocketChannel clientChannel = (SocketChannel) key.channel();
            try {
                if (clientChannel.isOpen() && clientChannel != null) {
                    buffer.clear();
                    clientChannel.write(buffer);
                    buffer.flip();
                    clientChannel.close();
                }
//                key.cancel();
            } catch (ClosedChannelException e ) {
               // System.out.println("read ß∞‹");
                //e.printStackTrace();
                key.cancel();
            }
            catch (IOException e) {
                // TODO: handle exception
            }
        }
    }
}
