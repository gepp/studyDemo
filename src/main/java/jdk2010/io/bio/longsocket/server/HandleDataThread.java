package jdk2010.io.bio.longsocket.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * 处理请求线程，这里用于写具体的业务逻辑，本例中只做了简单的实现
 * 
 * @author Martin
 * 
 */
public class HandleDataThread implements Runnable {
    // request from client
    private Socket request;

    // request id
    private int requestID;

    public HandleDataThread(Socket request, int requestID) {
        this.request = request;
        this.requestID = requestID;
    }

    @Override
    public void run() {
        try {
            // set timeout:4 seconds
            request.setSoTimeout(4000);

            while (true) {
                // get info from request when getting a socket request
                String reqStr = "";
                try {
                    // if read() get a timeout exception
                    reqStr = SocketUtil.readStrFromStream(request
                            .getInputStream());
                } catch (SocketTimeoutException e) {
                    // then break while loop, stop the service
                    System.out.println(SocketUtil.getNowTime() + " : Time is out, request[" + requestID + "] has been closed.");
                    break;
                }

                System.out.println(SocketUtil.getNowTime()
                        + " : request msg [" + reqStr + "].");

                // write response info;do not response when heart breaking.
                if ("Heart break".equals(reqStr)) {
                    SocketUtil.writeStr2Stream(
                            "hello client, this is server. requestID : "
                                    + requestID, request.getOutputStream());
                }
                
                else
                {
                    SocketUtil.writeStr2Stream(
                            "Server have got your msg.", request.getOutputStream());
                }
                
                Thread.sleep(1000);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (request != null) {
                try {
                    request.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
