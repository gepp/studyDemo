package jdk2010.io.nio2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerClientThread implements Runnable {
    SelectionKey selectionKey;
    static Selector selector;

    private static int BLOCK = 4096;
    private static int flag = 0;
    /* 接受数据缓冲区 */
    private static ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);
    /* 发送数据缓冲区 */
    private static ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);

    public ServerClientThread(SelectionKey selectionKey) throws IOException {
        this.selectionKey = selectionKey;
        this.selector = selectionKey.selector();
        //System.out.println("select count ："+selectionKey.selector().select());
        
    }

    @Override
    public void run() {
        handler(selectionKey);
        // 测试此键的通道是否已准备好接受新的套接字连接。

    }

    public static void handler(SelectionKey selectionKey) {
        ServerSocketChannel server = null;
        SocketChannel client = null;
        String receiveText;
        String sendText;
        int count = 0;
        System.out.println("================"+ Thread.currentThread().getName() + "running...");
        if (selectionKey.isAcceptable()) {
            // 返回为之创建此键的通道。
            
            ThreadGroup group = Thread.currentThread().getThreadGroup();
            Thread[] threads = new Thread[group.activeCount()];
            System.out.println("当前线程总数:" + group.activeCount());
            //group.enumerate(threads);
            // for (Thread thread : threads) {
            // if (thread == null) {
            // continue;
            // }
            // StringBuffer buf = new StringBuffer();
            // ThreadGroup tgroup = thread.getThreadGroup();
            // String groupName = tgroup == null ? "null" : tgroup.getName();
            // buf.append("CLIENTThreadGroup:").append(groupName).append(", ");
            // buf.append("Id:").append(thread.getId()).append(", ");
            // buf.append("Name:").append(thread.getName()).append(", ");
            // buf.append("isDaemon:").append(thread.isDaemon()).append(", ");
            // buf.append("isAlive:").append(thread.isAlive()).append(", ");
            // buf.append("Priority:").append(thread.getPriority());
            // System.out.println(buf.toString());
            // }
            server = (ServerSocketChannel) selectionKey.channel();
            try {
                // 接受到此通道套接字的连接。
                // 此方法返回的套接字通道（如果有）将处于阻塞模式。
                client = server.accept();
                System.out.println("接收到来自客户端（"
                        + client.socket().getInetAddress().getHostAddress()
                        + "）的连接");
                // 配置为非阻塞
                client.configureBlocking(false);
//              selectionKey.cancel();
                // 注册到selector，等待连接
                // System.out.println("selector-before:"+selector.select());
//              SelectionKey key = client.register(selector,
//                      SelectionKey.OP_READ);
                //client = (SocketChannel) selectionKey.channel();
                // 将缓冲区清空以备下次读取
                receivebuffer.clear();
                // 读取服务器发送来的数据到缓冲区中
                try {
                    count = client.read(receivebuffer);
                    if (count > 0) {
                        receiveText = new String(receivebuffer.array(), 0, count);
                        System.out.println("服务器端接受客户端数据--:" + receiveText);
                    //  client.register(selector, SelectionKey.OP_WRITE, receiveText);
                    } else {
                        System.out.println("read连接关闭");
                        //client.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                
                 
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("write结束,当前线程："
                    + Thread.currentThread().getName());
            // 将缓冲区清空以备下次写入
            sendbuffer.clear();
            // 返回为之创建此键的通道。
            //client = (SocketChannel) selectionKey.channel();
            sendText = "message from server--" + flag++;
            // 向缓冲区中输入数据
            sendbuffer.put(sendText.getBytes());
            // 将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
            sendbuffer.flip();
            // 输出到通道
            try {
                client.write(sendbuffer);
                System.out.println("服务器端向客户端发送数据--：" + sendText);
                client.close();

                //client.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //selectionKey.cancel();
        } else if (selectionKey.isReadable()) {
            System.out.println("accept结束,当前线程："
                    + Thread.currentThread().getName());
            // 返回为之创建此键的通道。
            client = (SocketChannel) selectionKey.channel();
            // 将缓冲区清空以备下次读取
            receivebuffer.clear();
            // 读取服务器发送来的数据到缓冲区中
            try {
                count = client.read(receivebuffer);

                if (count > 0) {
                    receiveText = new String(receivebuffer.array(), 0, count);
                    System.out.println("服务器端接受客户端数据--:" + receiveText);
                    client.register(selector, SelectionKey.OP_WRITE,
                            receiveText);
                } else {
                    System.out.println("read连接关闭");
                    client.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (selectionKey.isWritable()) {
            System.out.println("write结束,当前线程："
                    + Thread.currentThread().getName());
            // 将缓冲区清空以备下次写入
            sendbuffer.clear();
            // 返回为之创建此键的通道。
            client = (SocketChannel) selectionKey.channel();
            sendText = "message from server--" + flag++;
            // 向缓冲区中输入数据
            sendbuffer.put(sendText.getBytes());
            // 将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
            sendbuffer.flip();
            // 输出到通道
            try {
                client.write(sendbuffer);

                System.out.println("服务器端向客户端发送数据--：" + sendText);
                client.close();

                //client.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (selectionKey.isConnectable()) {

            // 输入结束，关闭 socketChannel
            System.out.println("connectable");
            try {
                client.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("其他");
        }

    }
    
    public static void read(SelectionKey selectionKey){
        
    }

}
