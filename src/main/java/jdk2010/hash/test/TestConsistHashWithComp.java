package jdk2010.hash.test;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jdk2010.hash.ConsistantHash;
import jdk2010.hash.Node;

/**
 * 一致性HASH测试类--多线程并发测试
 */
public class TestConsistHashWithComp {
    final ConcurrentHashMap<String, Long> stat = new ConcurrentHashMap<String, Long>();

    public static void main(String[] args) throws InterruptedException {
        final TestConsistHashWithComp testConsistHashWithComp = new TestConsistHashWithComp();
        Set<Node> ips = new HashSet<Node>();
        ips.add(new Node("192.168.1.1"));
        ips.add(new Node("192.168.1.2"));
        ips.add(new Node("192.168.1.3"));
        ips.add(new Node("192.168.1.4"));
        ips.add(new Node("192.168.1.5"));
//        ips.add(new Node("192.168.1.6")); 
        ips.add(new Node("192.168.1.7"));
        ips.add(new Node("192.168.1.8"));
        ips.add(new Node("192.168.1.9"));
        ips.add(new Node("192.168.1.10"));

        final ConsistantHash hash = ConsistantHash.getInstance();
        hash.setNodeList(ips);
//        hash.setAlg(HashAlgorithm.LUA_HASH);
//    	hash.set(1024);
//    	hash.setAlg(HashAlgorithm.CRC32_HASH);
        hash.buildHashCycle();

        long start = System.currentTimeMillis();

        for (int i = 0; i < 1; i++) {
            final String name = "thread" + i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int h = 1; h < 20; h++) {
                        Node node = hash.findNodeByKey(name + h);
                        System.out.println(h+"in "+node.getIp());
                        testConsistHashWithComp.send(node);
                    }
                    
                    //testConsistHashWithComp.print();
                }
            }, name);
            t.start();
        }
        System.out.println(System.currentTimeMillis() - start);
        Thread.sleep(1000 * 2);
        testConsistHashWithComp.print();
    }

    public synchronized void send(Node node) {
        Long count = stat.get(node.getIp());
        if (count == null) {
            stat.put(node.getIp(), 1L);
        } else {
            stat.put(node.getIp(), count + 1);
        }
    }

    public ConcurrentHashMap<String, Long> getStat() {
        return stat;
    }

    public void print() {
        long all = 0;
        for (Map.Entry<String, Long> entry : stat.entrySet()) {
            long num = entry.getValue();
            all += num;
            System.out.println("mac:" + entry.getKey() + " hits:" + num);
        }
        System.out.println("all：" + all);
    }
}
