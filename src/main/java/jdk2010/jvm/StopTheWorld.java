package jdk2010.jvm;

import java.util.HashMap;

public class StopTheWorld {
	public static Thread myThread=new Thread(new Runnable() {
		HashMap  map=new HashMap();
		@Override
		public void run() {
			while(true){
				while(map.size()/1024/1024>100){
					map.clear();
				}
				
				map.put(System.nanoTime(),new byte[512]);
			}
		}
	});
	
	public static Thread printThread=new Thread(new Runnable() {
		@Override
		public void run() {
			final long currentTime=System.currentTimeMillis();
			while(true){
				try {
					
					long nowTime=System.currentTimeMillis()-currentTime;
					System.out.println(nowTime/1000+"."+nowTime%1000);
					Thread.sleep(100);
					 
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	});
	
	public static void main(String[] args) {
		myThread.start();
		printThread.start();
	}
	
}
