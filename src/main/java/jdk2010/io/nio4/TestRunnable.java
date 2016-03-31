package jdk2010.io.nio4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class TestRunnable implements Runnable {
    CountDownLatch latch;
    
    int port;
    
    public TestRunnable (CountDownLatch latch,int port){
        this.latch=latch;
        this.port=port;
    }
    
    @Override
    public void run() {
        Socket socket;
        try {
            socket = new Socket("localhost", port);
            OutputStream out = socket.getOutputStream();
            out.write("<xml>aaa</xml>".getBytes());
            out.flush();
            socket.shutdownOutput();
            InputStream in = socket.getInputStream();
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = in.read(b)) != -1) {
                System.out.println(new String(b, 0, length));
            }
            out.close();
            in.close();
            socket.close();
            latch.countDown();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
