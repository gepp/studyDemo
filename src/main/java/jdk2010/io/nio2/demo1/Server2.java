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

public class Server2 {
    private final int PORT = 8080;
    private ExecutorService readPool;
    private ExecutorService writePool;
    private ServerSocketChannel serverSocketChannel;
    private static Selector selector;
    private static Charset charset = Charset.forName("utf-8");
    private int n;
    
     
    public static void main(String[] args) throws IOException{
        Server2 server = new Server2();
        server.doService();
    }
     
    public Server2() throws IOException{
    	readPool = Executors.newFixedThreadPool(5);
    	System.out.println("���̳߳���������СΪ5...");
    	writePool= Executors.newFixedThreadPool(5);
    	System.out.println("д�̳߳���������СΪ5...");
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
            }catch (IOException e) {
                throw new RuntimeException("Selector.select()�쳣!");
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
                try{
                if(key.isAcceptable()){
                    SocketChannel sc = null;
                    try{
                        sc = ((ServerSocketChannel)key.channel()).accept();
                        sc.configureBlocking(false);
                        System.out.println("�ͻ���:"+sc.socket().getInetAddress().getHostAddress()+"������");
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
                }catch(Exception e){
                	System.err.println("================="+e.getMessage());
                	
                }
                
                
            }
        }
    }
     
    public static class ReadWorker implements Runnable{
        private SelectionKey key;
        private   ByteBuffer receiveBuffer;
        private   ByteBuffer sendbuffer;
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
                while((len=client.read(receiveBuffer))>0){//�����������̶�ȡ�����������ֽ�
                	receiveBuffer.flip();
                	String receiveStr=charset.decode(receiveBuffer).toString();
                    System.out.println("�ͻ����������ݣ�"+receiveStr);
                    
                    String sendMsg=UUID.randomUUID()+"thread:"+receiveStr;
                     sendbuffer.clear();
                    //��װһ��
                    sendbuffer.put((sendMsg).getBytes());
                    sendbuffer.flip();
                    client.write(sendbuffer);
                    //System.out.println("��������:"+charset.decode(sendbuffer).toString());
                    client.close();
                    System.out.println("thread-->"+Thread.currentThread().getId());

                 }
                if(len==-1){
                	System.out.println("read��������������");
                    client.close();  //���عر�
                }
                //û�п����ֽ�,��������OP_READ
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
    
    
}
