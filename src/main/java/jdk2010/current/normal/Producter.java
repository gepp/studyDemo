package jdk2010.current.normal;

import java.util.List;

public class Producter extends Thread{
    List<Integer> list; 
    public Producter(List<Integer> list){
        this.list=list;
        start();
    }
    @Override
    public void run() {
        while(true){
        synchronized (list) {
            if(list.size()<100){
                System.out.println("生产商品编号:"+(list.size()+1));
                list.add(list.size()+1);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else{
                System.out.println("已满，过会生产");
            }
        }
        }
    }
 
    
}
