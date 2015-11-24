package jdk2010.io.nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//������򵥵ĵ�Reactor���߳�ģ��
public class SocktNioServerDemo {
	private static Selector selector;
	private int BLOCK = 4096;
	private int flag = 0;
	int totalCount;
	int keysLength;
	/* �������ݻ����� */
	private ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);
	/* �������ݻ����� */
	private ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);

	private static boolean threadFlag = false;
	private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();;

	public synchronized static boolean getThreadFlag() {
		return threadFlag;
	}

	private static void printKeyInfo(SelectionKey sk) {
		System.out.println();
		String s = new String();
		s+=sk.toString()+"===";
		s += ",read: " + sk.isReadable();
		s += ", accept: " + sk.isAcceptable();
		s += ", conn: " + sk.isConnectable();
		s += ", write: " + sk.isWritable();
		s += ", Valid: " + sk.isValid();
		System.out.println("===" + s + "===");
	}

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
		// ��Selectorע��Channel����������Ȥ���¼�,�ȴ�����
		SelectionKey s = serverSocketChannel.register(selector,
				SelectionKey.OP_ACCEPT);
		System.out.println("===������SelectionKey===");
		System.out.println("Server Start----8080:");
		// System.out.println("now select count:"+selector.select());
	}

	// ����
	private void listen() throws IOException, InterruptedException {
		while (true) {
			// ѡ��һ�����������Ӧ��ͨ���Ѿ���
			try {
  				keysLength = selector.select();
				System.out.println("1keysLength:" + keysLength);
				for (SelectionKey key : selector.keys()) {
					printKeyInfo(key);
				}

				if (keysLength == 0) {
					continue;
				} else if (keysLength > 0) {

					System.out.println("selector����");
					// ReentrantReadWriteLock lock=new ReentrantReadWriteLock();
					// ���ش�ѡ��������ѡ�������

					Set<SelectionKey> selectionKeys = selector.selectedKeys();
					Iterator<SelectionKey> iterator = selectionKeys.iterator();
					while (iterator.hasNext()) {
						SelectionKey selectionKey = iterator.next();
						iterator.remove();
						if (selectionKey.isAcceptable()) {
							ServerSocketChannel server = null;
							SocketChannel client = null;
							server = (ServerSocketChannel) selectionKey
									.channel();
							client = server.accept();
							System.out.println("���յ����Կͻ��ˣ�"
									+ client.socket().getInetAddress()
											.getHostAddress() + "��������");
							client.configureBlocking(false);
							client.register(selector,SelectionKey.OP_READ);
						} else {
							Thread a = new Thread(new ServerClientThread(
									selectionKey));
							a.start();
 						}
					}

				}
			} catch (Exception e) {

			} finally {
 			}
		}
	}

	public synchronized boolean containsKey(SelectionKey selectionKey) {
		boolean returnflag = false;
		Set<SelectionKey> selectionKeys = selector.selectedKeys();
		Iterator<SelectionKey> iterator = selectionKeys.iterator();
		while (iterator.hasNext()) {
			SelectionKey selectionKey1 = iterator.next();
			if (selectionKey == selectionKey1) {
				returnflag = true;
				break;
			}
		}
		System.out.println("=========================" + returnflag);
		return returnflag;
	}

	public static void main(String[] args) throws Exception {
		int port = 8080;
		SocktNioServerDemo server = new SocktNioServerDemo(port);
		server.listen();
	}

}
