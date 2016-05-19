package jdk2010.queue;

import java.util.PriorityQueue;
import java.util.TreeSet;

public class PriorityQueueTest {
    
    public void testTreeSet(){
        TreeSet<String> tree=new TreeSet<>();
        tree.add("c");
        tree.add("b");
        tree.add("d");
        tree.add("f");
        for(String i:tree){
            System.out.println(i);
        }
    }
    
    public static void main(String[] args) {
        PriorityQueue<String> q=new PriorityQueue<>();
        q.add("c");
        q.add("b");
        q.add("d");
        q.add("f");
        for(String i:q){
            System.out.println(i);
        }
        
        System.out.println("=========");
        new PriorityQueueTest().testTreeSet();
    }
}
