package jdk2010.current.daemon;

public class DemoTest1 {
	public static void main(String[] args) {
		Thread[] createdThreads=new Thread[Thread.activeCount()*10];
		ThreadGroup root = Thread.currentThread().getThreadGroup();
		ThreadGroup ttg = root;
		
		while((ttg = ttg.getParent())!=null){
			root=ttg;
		}
		root.enumerate(createdThreads);
		for(Thread thread:createdThreads){
			if(thread!=null)
			System.out.println("name:"+thread.getName()+",Priority:"+thread.getPriority());
		}
		
	}
}
