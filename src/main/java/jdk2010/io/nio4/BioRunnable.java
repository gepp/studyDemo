package jdk2010.io.nio4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class BioRunnable implements Runnable {
    CountDownLatch latch;

    int port;

    public BioRunnable(CountDownLatch latch, int port) {
        this.latch = latch;
        this.port = port;
    }

    @Override
    public void run() {
        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;

        try {
            socket = new Socket("localhost", port);
            socket.setSoTimeout(5000);
            socket.setSoLinger(true, 0); 
            System.out.println("����" + Thread.currentThread().getName());
            out = socket.getOutputStream();
            out.write("<xml>aaa</xml>1\r\n".getBytes());
            out.flush();
            out.write("<xml>aaa</xml>2".getBytes());
            socket.shutdownOutput();
            in = socket.getInputStream();
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = in.read(b)) != -1) {
                System.out.println(Thread.currentThread().getName() + new String(b, 0, length));
            }
            out.close();
            in.close();
            //socket.close();
            latch.countDown();
        } catch (IOException e) {
            try {
                if(out!=null)
                out.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                if(in!=null)
                in.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                if(socket!=null)
                socket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            finally
            {
                socket=null;
                latch.countDown();
                e.printStackTrace();
            }
           
        }
    }
}
