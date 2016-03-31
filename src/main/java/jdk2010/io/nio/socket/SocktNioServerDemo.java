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
//������򵥵ĵ�Reactor���߳�ģ��
public class SocktNioServerDemo {
	private static  Selector selector;
   
	private  int BLOCK = 4096;
	
    private  int flag = 0;
    
    private int keysLength;

    int totalCount;

	  /*�������ݻ�����*/
    private  ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);
    /*�������ݻ�����*/
    private  ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);
	
    public SocktNioServerDemo(int port) throws IOException {
        // �򿪷������׽���ͨ��
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // ����������Ϊ������
        serverSocketChannel.configureBlocking(false);
        // �������ͨ�������ķ������׽���
        ServerSocket serverSocket = serverSocketChannel.socket();
        // ���з���İ�
        serverSocket.bind(new InetSocketAddress(port));
        // ͨ��open()�����ҵ�Selector,Selector�ڲ�ԭ��ʵ��������һ������ע���channel����ѯ����
        selector = Selector.open();
        //��Selectorע��Channel����������Ȥ���¼�,�ȴ�����
        SelectionKey s = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("===������SelectionKey===");
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

	// ��������
	private static void send(String username, SocketChannel socketChannel)
			throws Exception {
		System.out.println("���أ�"+username);
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
 
	
	  // ����
    private void listen() throws IOException {
        while (true) {
            // ѡ��һ�����������Ӧ��ͨ���Ѿ���
        	keysLength=selector.select();
        	if(keysLength>0){
            // ���ش�ѡ��������ѡ�������
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
    // ��������
    private void handleKey(SelectionKey selectionKey) throws IOException {
        // ��������
    	totalCount++;
    	System.out.println("totalCount:"+totalCount);
        ServerSocketChannel server = null;
        SocketChannel client = null;
        String receiveText;
        String sendText;
        int count=0;
        
        // ���Դ˼���ͨ���Ƿ���׼���ý����µ��׽������ӡ�
        if (selectionKey.isAcceptable()) {
            // ����Ϊ֮�����˼���ͨ����
        	System.out.println("��ǰthread���ȣ�"+Thread.activeCount());
            server = (ServerSocketChannel) selectionKey.channel();
            // ���ܵ���ͨ���׽��ֵ����ӡ�
            // �˷������ص��׽���ͨ��������У�����������ģʽ��
            client = server.accept();
            // ����Ϊ������
            client.configureBlocking(false);
            // ע�ᵽselector���ȴ�����
            client.register(selector, SelectionKey.OP_READ,"abc");
        } else if (selectionKey.isReadable()) {
        	System.out.println("read:"+selectionKey.attachment());
            // ����Ϊ֮�����˼���ͨ����
            client = (SocketChannel) selectionKey.channel();
            //������������Ա��´ζ�ȡ
            receivebuffer.clear();
            //��ȡ�����������������ݵ���������
            count = client.read(receivebuffer);
            if (count > 0) {
                receiveText = new String( receivebuffer.array(),0,count);
                System.out.println("�������˽��ܿͻ�������--:"+receiveText);
                
                client.register(selector, SelectionKey.OP_WRITE,receiveText);
            }else{
            	System.out.println("read���ӹر�");
            	client.close();
            }
        } else if (selectionKey.isWritable()) {
        	System.out.println("write:"+selectionKey.attachment());
        	//������������Ա��´�д��
            sendbuffer.clear();
            // ����Ϊ֮�����˼���ͨ����
            client = (SocketChannel) selectionKey.channel();
            sendText="message from server--" + flag++;
            //�򻺳�������������
            sendbuffer.put(sendText.getBytes());
             //������������־��λ,��Ϊ������put�����ݱ�־���ı�Ҫ����ж�ȡ���ݷ��������,��Ҫ��λ
            sendbuffer.flip();
            //�����ͨ��
            client.write(sendbuffer);
            System.out.println("����������ͻ��˷�������--��"+sendText);
            client.close();
           // client.register(selector, SelectionKey.OP_READ);
        }else if(selectionKey.isConnectable()){
        	// ����������ر� socketChannel
        	System.out.println(selectionKey.attachment()+ " �ѹر�����");
        	client.close();
        }
    }


}
