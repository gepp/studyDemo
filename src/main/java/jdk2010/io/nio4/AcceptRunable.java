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
                    // �󶨵����ض˿�
                    System.out.println("���յ��ͻ����ӣ����ԣ�"+ socketChannel.socket().getInetAddress() + ":"    + socketChannel.socket().getPort());  
                    if (socketChannel.isConnected() && socketChannel != null) {
                        //System.out.println("socketChannel:" + socketChannel);
                        socketChannel.socket().setSoTimeout(5000);
                        socketChannel.configureBlocking(false);
                        socketChannel.register(NioServer4.getSelect(), SelectionKey.OP_WRITE);
                        socketChannel.register(NioServer4.getSelect(), SelectionKey.OP_READ);
                        //System.out.println(Thread.currentThread().getName() + "accept�򿪳ɹ�");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("acceptʧ��");
            e.printStackTrace();
            key.cancel();
        }

    }

}
