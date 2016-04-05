package jdk2010.io.nio5.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * @author zengjiantao
 * @date 2013-4-8
 */
public class PushClient extends Thread {

	private static final int BUFFER_SIZE = 1024;

	/**
	 * 远程地址
	 */
	private final InetSocketAddress mRemoteAddress;

	/**
	 * 连接通道
	 */
	private SocketChannel mSocketChannel;

	/**
	 * 接收缓冲区
	 */
	private final ByteBuffer mReceiveBuf;

	/**
	 * 端口选择器
	 */
	private Selector mSelector;

	/**
	 * 线程是否结束的标志
	 */
	private final AtomicBoolean mShutdown;

	static {
		java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
		java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
	}

	public PushClient(InetSocketAddress remoteAddress) {
		mRemoteAddress = remoteAddress;

		// 初始化缓冲区
		mReceiveBuf = ByteBuffer.allocateDirect(BUFFER_SIZE);
		if (mSelector == null) {
			// 创建新的Selector
			try {
				mSelector = Selector.open();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		mShutdown = new AtomicBoolean(false);
	}

	/**
	 * 打开通道
	 */
	private void startup() {
		try {
			// 打开通道
			mSocketChannel = SocketChannel.open();
			// 绑定到本地端口
			mSocketChannel.socket().setSoTimeout(30000);
			mSocketChannel.configureBlocking(false);
			if (mSocketChannel.connect(mRemoteAddress)) {
				System.out.println("开始建立连接:" + mRemoteAddress);
			}
			mSocketChannel.register(mSelector, SelectionKey.OP_CONNECT
					| SelectionKey.OP_READ, this);
			System.out.println(Thread.currentThread().getName()+"端口打开成功");

		} catch (final IOException e1) {
			e1.printStackTrace();
		}
	}

	private void select() {
		int nums = 0;
		try {
			if (mSelector == null) {
				return;
			}
			nums = mSelector.select(1000);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		// 如果select返回大于0，处理事件
		if (nums > 0) {
			Iterator<SelectionKey> iterator = mSelector.selectedKeys()
					.iterator();
			while (iterator.hasNext()) {
				// 得到下一个Key
				final SelectionKey key = iterator.next();
				iterator.remove();
				// 检查其是否还有效
				if (!key.isValid()) {
					continue;
				}
				// 处理事件
				try {
					if (key.isConnectable()) {
						connect();
					} else if (key.isReadable()) {
						read(key);
					}
				} catch (final Exception e) {
					e.printStackTrace();
					key.cancel();
				}
			}
		}
	}

	@Override
	public void run() {
		startup();
		// 启动主循环流程
		while (!mShutdown.get()) {
			try {
				// do select
				select();
				try {
					Thread.sleep(10);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		shutdown();
	}

	private void connect() throws IOException {
		if (isConnected()) {
			return;
		}
		// 完成SocketChannel的连接
		mSocketChannel.finishConnect();
		while (!mSocketChannel.isConnected()) {
			try {
				Thread.sleep(300);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			mSocketChannel.finishConnect();
		}

	}

	public void disConnect() {
		mShutdown.set(true);
	}

	private void shutdown() {
		if (isConnected()) {
			try {
				mSocketChannel.close();
				while (mSocketChannel.isOpen()) {
					try {
						Thread.sleep(300);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
					mSocketChannel.close();
				}
				System.out.println("端口关闭成功");
			} catch (final IOException e) {
				System.err.println("端口关闭错误:");
				e.printStackTrace();
			} finally {
				mSocketChannel = null;
			}
		} else {
			System.out.println("通道为空或者没有连接");
		}
		// 关闭端口选择器
		if (mSelector != null) {
			try {
				mSelector.close();
				System.out.println("端口选择器关闭成功");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				mSelector = null;
			}
		}
	}

	private void read(SelectionKey key) throws IOException {
		// 接收消息
		final byte[] msg = recieve();
		if (msg != null) {
			String tmp = new String(msg);
			System.out.println(Thread.currentThread().getName()+"返回内容："+tmp);
		}
	}

	private byte[] recieve() throws IOException {
		if (isConnected()) {
			int len = 0;
			int readBytes = 0;

			synchronized (mReceiveBuf) {
				mReceiveBuf.clear();
				try {
					while ((len = mSocketChannel.read(mReceiveBuf)) > 0) {
						readBytes += len;
					}
				} finally {
					mReceiveBuf.flip();
				}
				if (readBytes > 0) {
					final byte[] tmp = new byte[readBytes];
					mReceiveBuf.get(tmp);
					return tmp;
				} else {
					System.out.println("接收到数据为空,重新启动连接");
					return null;
				}
			}
		} else {
			System.out.println("端口没有连接");
		}
		return null;
	}

	private boolean isConnected() {
		return mSocketChannel != null && mSocketChannel.isConnected();
	}

	public static void main(String[] args) {
		// for (int i = 0; i < 1000; i++) {
		// new PushClient(new InetSocketAddress("192.168.139.27",
		// 9999)).start();
		// }
		for (int i = 0; i < 10; i++) {
			new PushClient(new InetSocketAddress("localhost", 9999))
					.start();
		}
	}
}
