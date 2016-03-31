package jdk2010.io.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
//这是最简单的单Reactor单线程模型
public class SocktNioServerDemo {
	private static  Selector selector;
   
	private  int BLOCK = 4096;
	
    private  int flag = 0;
    
    private int keysLength;

    int totalCount;

	  /*接受数据缓冲区*/
    private  ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);
    /*发送数据缓冲区*/
    private  ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);
	
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
        //向Selector注册Channel及我们有兴趣的事件,等待连接
        SelectionKey s = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("===服务器SelectionKey===");
        System.out.println("Server Start----8080:");
    }
    
    
	public static void main(String[] args) throws Exception {
		  	int port = 8080;
		  	SocktNioServerDemo server = new SocktNioServerDemo(port);
	        server.listen();
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
		System.out.println("返回："+username);
		ByteBuffer buffer = ByteBuffer.wrap(username.getBytes("utf-8"));
		socketChannel.write(buffer);
		socketChannel.socket().shutdownOutput();
	}
	private static void printKeyInfo(SelectionKey sk) {
//        String s = new String();
// 
//        s = "Att: " + (sk.attachment() == null ? "no" : "yes");
//        s += ", Read: " + sk.isReadable();
//        s += ", Acpt: " + sk.isAcceptable();
//        s += ", Cnct: " + sk.isConnectable();
//        s += ", Wrt: " + sk.isWritable();
//        s += ", Valid: " + sk.isValid();
//        s += ", interest: " + sk.interestOps();
//        s += ", ready: " +sk.readyOps();
//         s += ",--------------------------";
//        System.out.println(s);
    }
 
	
	  // 监听
    private void listen() throws IOException {
        while (true) {
            // 选择一组键，并且相应的通道已经打开
        	keysLength=selector.select();
        	if(keysLength>0){
            // 返回此选择器的已选择键集。
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                printKeyInfo(selectionKey);
                 iterator.remove();
                handleKey(selectionKey);
            }
        	}else{
        		System.out.println("Select finished without any keys.");
        	}
        }
    }
    // 处理请求
    private void handleKey(SelectionKey selectionKey) throws IOException {
        // 接受请求
    	totalCount++;
    	System.out.println("totalCount:"+totalCount);
        ServerSocketChannel server = null;
        SocketChannel client = null;
        String receiveText;
        String sendText;
        int count=0;
        
        // 测试此键的通道是否已准备好接受新的套接字连接。
        if (selectionKey.isAcceptable()) {
            // 返回为之创建此键的通道。
        	System.out.println("当前thread长度："+Thread.activeCount());
            server = (ServerSocketChannel) selectionKey.channel();
            // 接受到此通道套接字的连接。
            // 此方法返回的套接字通道（如果有）将处于阻塞模式。
            client = server.accept();
            // 配置为非阻塞
            client.configureBlocking(false);
            // 注册到selector，等待连接
            client.register(selector, SelectionKey.OP_READ,"abc");
        } else if (selectionKey.isReadable()) {
        	System.out.println("read:"+selectionKey.attachment());
            // 返回为之创建此键的通道。
            client = (SocketChannel) selectionKey.channel();
            //将缓冲区清空以备下次读取
            receivebuffer.clear();
            //读取服务器发送来的数据到缓冲区中
            count = client.read(receivebuffer);
            if (count > 0) {
                receiveText = new String( receivebuffer.array(),0,count);
                System.out.println("服务器端接受客户端数据--:"+receiveText);
                
                client.register(selector, SelectionKey.OP_WRITE,receiveText);
            }else{
            	System.out.println("read连接关闭");
            	client.close();
            }
        } else if (selectionKey.isWritable()) {
        	System.out.println("write:"+selectionKey.attachment());
        	//将缓冲区清空以备下次写入
            sendbuffer.clear();
            // 返回为之创建此键的通道。
            client = (SocketChannel) selectionKey.channel();
            sendText="message from server--" + flag++;
            //向缓冲区中输入数据
            sendbuffer.put(sendText.getBytes());
             //将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
            sendbuffer.flip();
            //输出到通道
            client.write(sendbuffer);
            System.out.println("服务器端向客户端发送数据--："+sendText);
            client.close();
           // client.register(selector, SelectionKey.OP_READ);
        }else if(selectionKey.isConnectable()){
        	// 输入结束，关闭 socketChannel
        	System.out.println(selectionKey.attachment()+ " 已关闭连接");
        	client.close();
        }
    }


}
