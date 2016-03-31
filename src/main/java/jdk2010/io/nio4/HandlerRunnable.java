package jdk2010.io.nio4;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class HandlerRunnable implements Runnable {
    SelectionKey key;

    public HandlerRunnable(SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {
        if (key.channel().isOpen()) {
            System.out.println(Thread.currentThread().getName() + "====¶Á====");
            SocketChannel clientChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int length = 0;
            try {
                while ((length = clientChannel.read(buffer)) != -1) {
                    buffer.flip();
                    System.out.println(new String(buffer.array()));
                    buffer.clear();
                }
                //clientChannel.register(key.selector(), SelectionKey.OP_WRITE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer.clear();
            buffer.flip();
            System.out.println(Thread.currentThread().getName() + "====»ØÐ´====");
             buffer = ByteBuffer.wrap("return data".getBytes());
            try {
                clientChannel.write(buffer);
                clientChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
//            if (key.isAcceptable()) {
//                ServerSocketChannel server = (ServerSocketChannel) key.channel();
//                SocketChannel clientChannel;
//                try {
//                    clientChannel = server.accept();
//                    clientChannel.configureBlocking(false);
//                    clientChannel.register(key.selector(), SelectionKey.OP_READ);
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            } else if (key.isReadable()) {
//                System.out.println(Thread.currentThread().getName() + "====¶Á====");
//                SocketChannel clientChannel = (SocketChannel) key.channel();
//                ByteBuffer buffer = ByteBuffer.allocate(1024);
//                int length = 0;
//                try {
//                    while ((length = clientChannel.read(buffer)) != -1) {
//                        buffer.flip();
//                        System.out.println(new String(buffer.array()));
//                        buffer.clear();
//                    }
//                    clientChannel.register(key.selector(), SelectionKey.OP_WRITE);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else if (key.isWritable()) {
//                System.out.println(Thread.currentThread().getName() + "====»ØÐ´====");
//                ByteBuffer buffer = ByteBuffer.wrap("return data".getBytes());
//                SocketChannel clientChannel = (SocketChannel) key.channel();
//                try {
//                    System.out.println("key.channel().isOpen():"+key.channel().isOpen());
//                    clientChannel.write(buffer);
//                    clientChannel.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }else {  
            try {
                key.channel().close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  
        }  
    }
}
