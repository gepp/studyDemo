package jdk2010.io.nio2.demo1;

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
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server1 {
    private final int PORT = 8080;
    private ExecutorService readPool;
    private ExecutorService writePool;
    private ServerSocketChannel serverSocketChannel;
    private static Selector selector;
    private static Charset charset = Charset.forName("utf-8");
    private int n;
      
    public static void main(String[] args) throws IOException{
        Server1 server = new Server1();
        server.doService();
    }
     
    public Server1() throws IOException{
    	readPool = Executors.newFixedThreadPool(5);
    	System.out.println("读线程池启动，大小为5...");
    	writePool= Executors.newFixedThreadPool(5);
    	System.out.println("写线程池启动，大小为5...");
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket ss = serverSocketChannel.socket();
        ss.bind(new InetSocketAddress(PORT));
        selector = Selector.open();
        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
         System.out.println("Server started at "+PORT+"...");
    }
     
    public void doService(){
        while(true){
            try{
                n = selector.select();
                System.out.println("abc================");
            }catch (IOException e) {
                throw new RuntimeException("Selector.select()异常!");
            }
            if(n==0)
            {
                System.out.print(".");
                continue;
            }
             Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();
            while(iter.hasNext()){
                SelectionKey key = iter.next();
                iter.remove();
                if(key.isAcceptable()){
                    SocketChannel sc = null;
                    try{
                        sc = ((ServerSocketChannel)key.channel()).accept();
                        sc.configureBlocking(false);
                        System.out.println("客户端:"+sc.socket().getInetAddress().getHostAddress()+"已连接");
                        sc.register(selector, SelectionKey.OP_READ);
                    }catch (Exception e) {
                        try{
                            sc.close();
                        }catch (Exception ex) {
                        }
                    }
                }
                else if(key.isReadable()){
                    key.interestOps(key.interestOps()&(~SelectionKey.OP_READ));
                    readPool.execute(new ReadWorker(key));
                }
                else if(key.isWritable()){
                    key.interestOps(key.interestOps()&(~SelectionKey.OP_WRITE));
                    writePool.execute(new WriteWorker(key));
                }
            }
        }
    }
     
    public static class ReadWorker implements Runnable{
        private SelectionKey key;
        ByteBuffer receiveBuffer ;
        ByteBuffer sendbuffer;
        public ReadWorker(SelectionKey key){
            this.key = key;
          receiveBuffer =ByteBuffer.allocate(1024);
          sendbuffer = ByteBuffer.allocate(1024);
        }
         
        @Override
        public void run() {
            SocketChannel client = (SocketChannel)key.channel();
            receiveBuffer.clear();
            int len = 0;
            try{
                while((len=client.read(receiveBuffer))>0){//非阻塞，立刻读取缓冲区可用字节
                	receiveBuffer.flip();
                	String receiveStr=charset.decode(receiveBuffer).toString();
                    System.out.println("客户端请求数据："+receiveStr);
                    client.register(selector, SelectionKey.OP_WRITE,receiveStr);
                 }
                if(len==-1){
                	System.out.println("read操作结束。。。");
                   // client.close();  //不必关闭
                }
                //没有可用字节,继续监听OP_READ
                key.interestOps(key.interestOps()|SelectionKey.OP_READ);
                key.selector().wakeup();
            }catch (Exception e) {
                try {
                	client.close();
                } catch (IOException e1) {
                }
            }
        }
    }
    
    public static class WriteWorker implements Runnable{
        private SelectionKey key;
        ByteBuffer receiveBuffer ;
        ByteBuffer sendbuffer;
        public WriteWorker(SelectionKey key){
            this.key = key;
            receiveBuffer =ByteBuffer.allocate(1024);
            sendbuffer = ByteBuffer.allocate(1024);
        }
         
        @Override
        public void run() {
            SocketChannel client = (SocketChannel)key.channel();
            try{
             	String sendMsg=key.attachment()+"";
                    System.out.println("read读取数据:"+sendMsg);
                    sendbuffer.clear();
                    //包装一下
                    sendbuffer.put((sendMsg+"-->thread").getBytes());
                    sendbuffer.flip();
                    client.write(sendbuffer);
                    System.out.println("返回数据:"+sendMsg+"-->write");
                    client.close();
                 //没有可用字节,继续监听OP_WRITE
               key.interestOps(key.interestOps()|SelectionKey.OP_WRITE);
                key.selector().wakeup();
            }catch (Exception e) {
                try {
                	client.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
