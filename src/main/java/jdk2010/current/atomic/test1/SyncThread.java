package jdk2010.current.atomic.test1;

public class SyncThread implements Runnable{
    
    static final int  maxCount=10000;
    
    int count=0;
    
    AtomicIntegerSyncTest test;
    
    public SyncThread(AtomicIntegerSyncTest test){
        this.test=test;
    }
    
    @Override
    public void run() {
        while(test.getCount()<maxCount){
             test.add();
             
        }
        
        
    }

}
