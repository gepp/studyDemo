package jdk2010.current.atomic;

public class AtomicTest {
		public static void main(String[] args) throws InterruptedException {
			int result=1000;
			int maxLoop=1000;
			int beginLoop=0;
			ThreadGroup tg=Thread.currentThread().getThreadGroup();
			while(beginLoop++<=maxLoop){
				AtomicRunnable run=new AtomicRunnable();
				for(int i=0;i<maxLoop;i++){
					 new Thread(run).start();
					// System.out.println("activeCount:"+tg.activeCount());
				}
				while(tg.activeCount()!=1){
					Thread.sleep(15);
				}
				//System.out.println("��"+beginLoop+"�ν��:"+run.getCount());
				if(run.getCount()!=result){
					System.out.println("��"+beginLoop+"�ν��������,���Ϊ��"+run.getCount());
				}
				
			}
			
			
		}
}
