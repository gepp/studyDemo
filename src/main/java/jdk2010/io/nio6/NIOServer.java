package jdk2010.io.nio6;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NIOServer {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        int port = 7889;
        // ��ѡ����
        Selector selector = Selector.open();
        // �򿪷������׽���ͨ��
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // �������ͨ�������ķ������׽���
        ServerSocket serverSocket = ssc.socket();
        // �� ServerSocket �󶨵��ض���ַ��IP ��ַ�Ͷ˿ںţ�
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("server listen on port:" + port);

        // ����ͨ��������ģʽ
        ssc.configureBlocking(false);
        // �������ѡ����ע���ͨ��������һ��ѡ�����SelectionKey.OP_ACCEPT--�����׽��ֽ��ܲ����Ĳ�����λ
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // timeout:Ϊ�������ڵȴ�ĳ��ͨ��׼������ʱ������� timeout ���룻���Ϊ�㣬�������ڵ�����������Ϊ�Ǹ���
            int nKeys = selector.select(1000);
            if (nKeys > 0) {

                for (SelectionKey key : selector.selectedKeys()) {
                    /*
                     * ���Դ˼���ͨ���Ƿ���׼���ý����µ��׽�������-- ����˼���ͨ����֧���׽��ֽ��ܲ�������˷���ʼ�շ��� false
                     */
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel sc = server.accept();

                        if (sc == null) {
                            continue;
                        }
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        // ����һ���µ��ֽڻ�����
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        SocketChannel sc = (SocketChannel) key.channel();
                        int readBytes = 0;
                        String message = null;
                        try {
                            int ret;
                            try {
                                while ((ret = sc.read(buffer)) > 0) {
                                    readBytes += ret;
                                }

                            } catch (Exception e) {
                                readBytes = 0;
                                // ignore
                            } finally {
                                // ��ת�˻����������ȶԵ�ǰλ���������ƣ�Ȼ�󽫸�λ������Ϊ��
                                buffer.flip();
                            }

                            if (readBytes > 0) {
                                message = Charset.forName("UTF-8").decode(buffer).toString();
                                buffer = null;
                            }
                        } finally {
                            if (buffer != null)
                                buffer.clear();
                        }

                        if (readBytes > 0) {
                            System.out.println("message from client:" + message);
                            if ("quit".equalsIgnoreCase(message.trim())) {
                                sc.close();
                                selector.close();
                                System.out.println("Server has been shutdown!");
                                System.exit(0);
                            }
                            String outMessage = "server response:" + message;
                            sc.write(Charset.forName("UTF-8").encode(outMessage));
                        }

                    }
                }
                selector.selectedKeys().clear();
            }

        }
    }
}
