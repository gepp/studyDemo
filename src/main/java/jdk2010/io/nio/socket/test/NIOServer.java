package jdk2010.io.nio.socket.test;

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

public class NIOServer {
    
    private static  Selector selector;
    
    public NIOServer(int port) throws IOException{
        ServerSocketChannel serverChannel=ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        
        ServerSocket serverSocket=serverChannel.socket();
                     serverChannel.bind(new InetSocketAddress(port));
        
        selector=Selector.open();
        System.out.println("before-selector.size:"+selector.keys().size());
        serverChannel.register(selector,SelectionKey.OP_ACCEPT);
        System.out.println("after-selector.size:"+selector.keys().size());
        
    }
    public  void listen() throws IOException{
        while(true){
             int length=selector.select();
             System.out.println("length:"+length);
             if(length>0){
                Iterator<SelectionKey> iterator= selector.selectedKeys().iterator();
                SelectionKey key=null;
                while(iterator.hasNext()){
                    key=iterator.next();
                    iterator.remove();
                    handleSelectionKey(key);
                }
                 
             }else{
                 System.out.println("no selected");
             }
        }
    }
    public static void handleSelectionKey(SelectionKey key) throws IOException{
        ServerSocketChannel server=null;
        SocketChannel  client=null;
        if(key.isAcceptable()){
            server=(ServerSocketChannel)key.channel();
            server.configureBlocking(false);
            client=server.accept();
            client.configureBlocking(false);
            client.register(selector,SelectionKey.OP_READ);
        }
        else if(key.isReadable()){
            client=(SocketChannel)key.channel();
            ByteBuffer byteBuffer=ByteBuffer.allocate(1024*4);
            int length=0;
            String s="";
            while((length=client.read(byteBuffer))!=-1){
                byteBuffer.flip();
                if(length>0){
                s=Charset.forName("utf-8").decode(byteBuffer).toString();
                System.out.println("客户端请求:"+s);
                }
                byteBuffer.clear();
            }
            
            client.register(selector,SelectionKey.OP_WRITE,s);
        }
        else if(key.isWritable()){
            client=(SocketChannel)key.channel();
            ByteBuffer byteBuffer=ByteBuffer.allocate(1024*4);
            String s=key.attachment().toString();
            client.write(ByteBuffer.wrap(("哈哈"+s).getBytes("utf-8")));
            System.out.println("服务器返回："+"哈哈"+s);
            client.close();
        }
    }
    
    public static void main(String[] args) throws IOException {
            int port = 8080;
            NIOServer server=new NIOServer(8080);
            server.listen();
    }
}
