package jdk2010.current.reentrant;

public class Producter {
    private Depot depot;

    public Producter(Depot depot) {
        this.depot = depot;
    }

    // 消费产品：新建一个线程向仓库中生产产品。
    public void add(final int val) {
        new Thread() {
            public void run() {
                System.out.println(Thread.currentThread().getName()+"新增:"+val);
                depot.add(val);
            }
        }.start();
    }
}
