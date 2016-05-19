package jdk2010.juc.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapTest {
    public static void main(String[] args) {
        Map<String,Object> map=new LinkedHashMap<>();
        for(int i=0;i<100000;i++)
        map.put("a", "a");
         
        System.out.println(map.size()); 
        
        for(String s:map.keySet()){
            System.out.println(s);
        }
    }
}
