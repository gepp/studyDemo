package jdk2010.current.volatiletest;
import jdk2010.current.normal.RunTest1;

public class SyncTest {
        public static void main(String[] args) throws InterruptedException {
//          int result=100;
//          int maxLoop=100;
//          int beginLoop=0;
//          ThreadGroup tg=Thread.currentThread().getThreadGroup();
//          while(beginLoop++<=maxLoop){
//              SyncRunnable run=new SyncRunnable();
//              for(int i=0;i<maxLoop;i++){
//                   new Thread(run).start();
//                   System.out.println("activeCount:"+tg.activeCount());
//              }
//              while(tg.activeCount()!=1){
//                  
//                  Thread.sleep(15);
//              }
//              if(run.getCount()!=result){
//                  System.out.println("第"+beginLoop+"次结果有问题,结果为："+run.getCount());
//              }
//              
//              
//          }
            
            final ThreadGroup tg=Thread.currentThread().getThreadGroup();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("线程1");
                        Thread.sleep(1000);
                        System.out.println("activeCount1:"+tg.activeCount());
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("线程2");
                        Thread.sleep(2000);
                        System.out.println("activeCount2:"+tg.activeCount());
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
            System.out.println("activeCount:"+tg.activeCount());
            
        }
}
