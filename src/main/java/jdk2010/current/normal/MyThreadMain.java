package jdk2010.current.normal;

public class MyThreadMain {
    public static void main(String[] args) {
    
//      MyThread thread=new MyThread();
//      System.out.println("主线程："+Thread.currentThread().getName());
//      System.out.println("新线程:"+thread.getName());
//      //       thread.run();
//      //1. run()和其他方法的调用没任何不同,main方法按顺序执行了它,并打印出最后一句
//      thread.setDaemon(true);
//      thread.start();
//      System.out.println("abc");
//      System.out.println("主线程："+Thread.currentThread().getName());
//      System.out.println("新线程:"+thread.getName());
        
        Thread t1=new MyThread();
        Thread t2=new MyThread();
        t1.start();
        t2.start();
    }
}
