package jdk2010.io.nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//这是最简单的单Reactor单线程模型
public class SocktNioServerDemo {
    private static Selector selector;
    private int BLOCK = 4096;
    private int flag = 0;
    int totalCount;
    int keysLength;
    /* 接受数据缓冲区 */
    private ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);
    /* 发送数据缓冲区 */
    private ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);

    private static boolean threadFlag = false;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();;

    public synchronized static boolean getThreadFlag() {
        return threadFlag;
    }

    private static void printKeyInfo(SelectionKey sk) {
        System.out.println();
        String s = new String();
        s+=sk.toString()+"===";
        s += ",read: " + sk.isReadable();
        s += ", accept: " + sk.isAcceptable();
        s += ", conn: " + sk.isConnectable();
        s += ", write: " + sk.isWritable();
        s += ", Valid: " + sk.isValid();
        System.out.println("===" + s + "===");
    }

    public SocktNioServerDemo(int port) throws IOException {
        // 打开服务器套接字通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 服务器配置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 检索与此通道关联的服务器套接字
        ServerSocket serverSocket = serverSocketChannel.socket();
        // 进行服务的绑定
        serverSocket.bind(new InetSocketAddress(port));
        // 通过open()方法找到Selector,Selector内部原理实际是在做一个对所注册的channel的轮询访问
        selector = Selector.open();
        // 向Selector注册Channel及我们有兴趣的事件,等待连接
        SelectionKey s = serverSocketChannel.register(selector,
                SelectionKey.OP_ACCEPT);
        System.out.println("===服务器SelectionKey===");
        System.out.println("Server Start----8080:");
        // System.out.println("now select count:"+selector.select());
    }

    // 监听
    private void listen() throws IOException, InterruptedException {
        while (true) {
            // 选择一组键，并且相应的通道已经打开
            try {
                keysLength = selector.select();
                System.out.println("1keysLength:" + keysLength);
                for (SelectionKey key : selector.keys()) {
                    printKeyInfo(key);
                }

                if (keysLength == 0) {
                    continue;
                } else if (keysLength > 0) {

                    System.out.println("selector唤醒");
                    // ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
                    // 返回此选择器的已选择键集。

                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel server = null;
                            SocketChannel client = null;
                            server = (ServerSocketChannel) selectionKey
                                    .channel();
                            client = server.accept();
                            System.out.println("接收到来自客户端（"
                                    + client.socket().getInetAddress()
                                            .getHostAddress() + "）的连接");
                            client.configureBlocking(false);
                            client.register(selector,SelectionKey.OP_READ);
                        } else {
                            Thread a = new Thread(new ServerClientThread(
                                    selectionKey));
                            a.start();
                        }
                    }

                }
            } catch (Exception e) {

            } finally {
            }
        }
    }

    public synchronized boolean containsKey(SelectionKey selectionKey) {
        boolean returnflag = false;
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            SelectionKey selectionKey1 = iterator.next();
            if (selectionKey == selectionKey1) {
                returnflag = true;
                break;
            }
        }
        System.out.println("=========================" + returnflag);
        return returnflag;
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        SocktNioServerDemo server = new SocktNioServerDemo(port);
        server.listen();
    }

}
