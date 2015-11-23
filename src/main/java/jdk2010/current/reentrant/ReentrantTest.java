package jdk2010.current.reentrant;

public class ReentrantTest {
    public static void main(String[] args) throws InterruptedException {
        
        for(int i=0;i<5;i++){
            final Depot store=new Depot();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("======begin=====");
                
                Producter producter=new Producter(store);
                Customer customer=new Customer(store);
                producter.add(60);
                producter.add(120);
                producter.add(810);
                producter.add(100);
                customer.subtract(90);
                customer.subtract(150);
                producter.add(110);
                System.out.println("=====end======");
                 
                
            }
        }).start(); 
        while(Thread.activeCount()!=1){
            Thread.sleep(15);
        } 
        System.out.println("结果======="+store.getStore());
        
        }
                 
        
        //System.out.println(store.getStore());
        
    }
}
