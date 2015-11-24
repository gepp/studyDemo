package jdk2010.io.nio2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerClientThread implements Runnable {
	SelectionKey selectionKey;
	static Selector selector;

	private static int BLOCK = 4096;
	private static int flag = 0;
	/* �������ݻ����� */
	private static ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);
	/* �������ݻ����� */
	private static ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);

	public ServerClientThread(SelectionKey selectionKey) throws IOException {
		this.selectionKey = selectionKey;
		this.selector = selectionKey.selector();
 		//System.out.println("select count ��"+selectionKey.selector().select());
		
	}

	@Override
	public void run() {
		handler(selectionKey);
		// ���Դ˼���ͨ���Ƿ���׼���ý����µ��׽������ӡ�

	}

	public static void handler(SelectionKey selectionKey) {
		ServerSocketChannel server = null;
		SocketChannel client = null;
		String receiveText;
		String sendText;
		int count = 0;
		System.out.println("================"+ Thread.currentThread().getName() + "running...");
		if (selectionKey.isAcceptable()) {
			// ����Ϊ֮�����˼���ͨ����
			
			ThreadGroup group = Thread.currentThread().getThreadGroup();
			Thread[] threads = new Thread[group.activeCount()];
			System.out.println("��ǰ�߳�����:" + group.activeCount());
			//group.enumerate(threads);
			// for (Thread thread : threads) {
			// if (thread == null) {
			// continue;
			// }
			// StringBuffer buf = new StringBuffer();
			// ThreadGroup tgroup = thread.getThreadGroup();
			// String groupName = tgroup == null ? "null" : tgroup.getName();
			// buf.append("CLIENTThreadGroup:").append(groupName).append(", ");
			// buf.append("Id:").append(thread.getId()).append(", ");
			// buf.append("Name:").append(thread.getName()).append(", ");
			// buf.append("isDaemon:").append(thread.isDaemon()).append(", ");
			// buf.append("isAlive:").append(thread.isAlive()).append(", ");
			// buf.append("Priority:").append(thread.getPriority());
			// System.out.println(buf.toString());
			// }
			server = (ServerSocketChannel) selectionKey.channel();
			try {
				// ���ܵ���ͨ���׽��ֵ����ӡ�
				// �˷������ص��׽���ͨ��������У�����������ģʽ��
				client = server.accept();
				System.out.println("���յ����Կͻ��ˣ�"
						+ client.socket().getInetAddress().getHostAddress()
						+ "��������");
				// ����Ϊ������
				client.configureBlocking(false);
//				selectionKey.cancel();
				// ע�ᵽselector���ȴ�����
 				// System.out.println("selector-before:"+selector.select());
//				SelectionKey key = client.register(selector,
//						SelectionKey.OP_READ);
 				//client = (SocketChannel) selectionKey.channel();
				// ������������Ա��´ζ�ȡ
				receivebuffer.clear();
				// ��ȡ�����������������ݵ���������
				try {
					count = client.read(receivebuffer);
					if (count > 0) {
						receiveText = new String(receivebuffer.array(), 0, count);
						System.out.println("�������˽��ܿͻ�������--:" + receiveText);
					//	client.register(selector, SelectionKey.OP_WRITE, receiveText);
					} else {
						System.out.println("read���ӹر�");
						//client.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			     
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("write����,��ǰ�̣߳�"
					+ Thread.currentThread().getName());
			// ������������Ա��´�д��
			sendbuffer.clear();
			// ����Ϊ֮�����˼���ͨ����
			//client = (SocketChannel) selectionKey.channel();
			sendText = "message from server--" + flag++;
			// �򻺳�������������
			sendbuffer.put(sendText.getBytes());
			// ������������־��λ,��Ϊ������put�����ݱ�־���ı�Ҫ����ж�ȡ���ݷ��������,��Ҫ��λ
			sendbuffer.flip();
			// �����ͨ��
			try {
				client.write(sendbuffer);
				System.out.println("����������ͻ��˷�������--��" + sendText);
				client.close();

				//client.register(selector, SelectionKey.OP_READ);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//selectionKey.cancel();
		} else if (selectionKey.isReadable()) {
			System.out.println("accept����,��ǰ�̣߳�"
					+ Thread.currentThread().getName());
			// ����Ϊ֮�����˼���ͨ����
			client = (SocketChannel) selectionKey.channel();
			// ������������Ա��´ζ�ȡ
			receivebuffer.clear();
			// ��ȡ�����������������ݵ���������
			try {
				count = client.read(receivebuffer);

				if (count > 0) {
					receiveText = new String(receivebuffer.array(), 0, count);
					System.out.println("�������˽��ܿͻ�������--:" + receiveText);
					client.register(selector, SelectionKey.OP_WRITE,
							receiveText);
				} else {
					System.out.println("read���ӹر�");
					client.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (selectionKey.isWritable()) {
			System.out.println("write����,��ǰ�̣߳�"
					+ Thread.currentThread().getName());
			// ������������Ա��´�д��
			sendbuffer.clear();
			// ����Ϊ֮�����˼���ͨ����
			client = (SocketChannel) selectionKey.channel();
			sendText = "message from server--" + flag++;
			// �򻺳�������������
			sendbuffer.put(sendText.getBytes());
			// ������������־��λ,��Ϊ������put�����ݱ�־���ı�Ҫ����ж�ȡ���ݷ��������,��Ҫ��λ
			sendbuffer.flip();
			// �����ͨ��
			try {
				client.write(sendbuffer);

				System.out.println("����������ͻ��˷�������--��" + sendText);
				client.close();

				//client.register(selector, SelectionKey.OP_READ);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (selectionKey.isConnectable()) {

			// ����������ر� socketChannel
			System.out.println("connectable");
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("����");
		}

	}
	
	public static void read(SelectionKey selectionKey){
		
	}

}
