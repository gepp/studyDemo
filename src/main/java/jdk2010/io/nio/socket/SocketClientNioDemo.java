package jdk2010.io.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class SocketClientNioDemo {
    public static void main(String[] args) throws Exception {
//      SocketChannel socketChannel = SocketChannel.open();
//      socketChannel.connect(new InetSocketAddress("localhost", 8080));
//      ByteBuffer buffer = ByteBuffer.wrap("小明".getBytes("utf-8"));
//      socketChannel.write(buffer);
//      socketChannel.socket().shutdownOutput();
//      String username = receive(socketChannel);
//      System.out.println(username);
        
        for(int i=0;i<500;i++){
            SocketChannel socketChannel = SocketChannel.open();
            new ThreadClient(socketChannel,i);
        }
        
    }

    private static String receive(SocketChannel socketChannel) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int length = 0;
        String username = "";
        while ((length = socketChannel.read(buffer)) != -1) {
            
            buffer.flip();
            username = username
                    + Charset.forName("UTF-8").decode(buffer).toString();
            buffer.clear();
        }

        return username;

    }

    // 发送数据
    private static void send(String username, SocketChannel socketChannel)
            throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(username.getBytes("utf-8"));
        socketChannel.write(buffer);
        socketChannel.socket().shutdownOutput();
    }

}
