package jdk2010.mianshi.question1;

public class Function1 {
    static Object obj = new Object();

    static String str = "A";

    static int count = 100000;

    public static void main(String[] args) throws InterruptedException {
        long beginTime=System.currentTimeMillis();
        Thread thread1;
        Thread thread2;
        Thread thread3;
        ThreadGroup tg = new ThreadGroup("test");
        thread1 = new Thread(tg,new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count;) {
                    synchronized (obj) {
                        if (str == "A") {
                            System.out.println(i + "A");
                            str = "B";
                            i++;
                            obj.notifyAll();
                        } else {
                            try {
                                obj.wait();

                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        });

        thread2 = new Thread(tg,new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count;) {
                    synchronized (obj) {
                        if (str == "B") {
                            System.out.println(i + "B");
                            str = "C";
                            i++;
                            obj.notifyAll();

                        } else {
                            try {
                                obj.wait();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        });

        thread3 = new Thread(tg,new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count;) {
                    synchronized (obj) {
                        if (str == "C") {
                            System.out.println(i + "C");
                            str = "A";
                            i++;
                            obj.notifyAll();
                        } else {
                            try {
                                obj.wait();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        
        while(true){
            if(tg.activeCount()==0){
                long endTime=System.currentTimeMillis();
                System.out.println("need :"+(endTime-beginTime)+"ms");
                break;
            }else{
                Thread.sleep(100);
            }
        }
    }
}
