package jdk2010.io.nio4;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ReadAndWriteRunnable implements Runnable {
    SelectionKey key;

    public ReadAndWriteRunnable(SelectionKey key) {
        this.key = key;
    }

    @Override
    public void run() {
        if (key.isValid()) {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            if (clientChannel.isConnected() && clientChannel != null) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int length = 0;
                try {
                    clientChannel.socket().sendUrgentData(0xFF);
                    String fromMessage="";
                    while ((length = clientChannel.read(buffer)) > 0) {
                        System.out.println("length:"+length);
                        buffer.flip();
                        fromMessage=fromMessage+new String(buffer.array(), 0, length);
                        //readBuffer.put(buffer);
                        buffer.clear();
                    }
                    if(!fromMessage.equals("")){
                        String returnData="server back:"+fromMessage+"\n";
                        System.out.println(Thread.currentThread().getName()+"message from client:" + fromMessage);
                        clientChannel.write(ByteBuffer.wrap(returnData.getBytes()));
                       // clientChannel.close();
                    }
                    
                    
                }
                catch (ClosedChannelException e ) {
                    //System.out.println("read ß∞‹");
                    e.printStackTrace();
                    key.cancel();
                }
                catch (IOException e) {
                    // TODO: handle exception
                }finally{
                    buffer.flip();
                    buffer.clear();
                }
            }
        }
    }
}
