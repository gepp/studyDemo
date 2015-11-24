package jdk2010.current.daemon;

public class Main {
		public static void main(String[] args) throws InterruptedException {
			Thread deamonThread=new Thread(new DaemonThread());
			deamonThread.setDaemon(true);
			deamonThread.start();
			for (int i = 0; i < 10; i++) {
				System.out.println("Main thread: " + i);
				Thread.sleep(1000);
			}
		}
}
