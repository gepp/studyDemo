package jdk2010.io.nio4;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AcceptRunable implements Runnable {

    private final SelectionKey key;

    public AcceptRunable(SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {
        try {
            if(key.isValid()){
                SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                if (socketChannel != null) {
                    // 绑定到本地端口
                    System.out.println("接收到客户连接，来自："+ socketChannel.socket().getInetAddress() + ":"    + socketChannel.socket().getPort());  
                    if (socketChannel.isConnected() && socketChannel != null) {
                        //System.out.println("socketChannel:" + socketChannel);
                        socketChannel.socket().setSoTimeout(5000);
                        socketChannel.configureBlocking(false);
                        socketChannel.register(NioServer4.getSelect(), SelectionKey.OP_WRITE);
                        socketChannel.register(NioServer4.getSelect(), SelectionKey.OP_READ);
                        //System.out.println(Thread.currentThread().getName() + "accept打开成功");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("accept失败");
            e.printStackTrace();
            key.cancel();
        }

    }

}
