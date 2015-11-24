package jdk2010.current.reentrant;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Depot {
	private int store;
	public void setStore(int store) {
		this.store = store;
	}
	private int capacity;
	private Lock lock;
	private Condition addCondition;
	private Condition substractCondition;
	
	public Depot(){
		this.store=0;
		this.capacity=1000;
		lock=new ReentrantLock();
		addCondition=lock.newCondition();
		substractCondition=lock.newCondition();
	}
	
	public void add(int count){
		try{
		lock.lock();
		 if((count+store)>=capacity){
			 System.out.println(Thread.currentThread().getName()+"已满");
			 addCondition.await();
			 System.out.println(Thread.currentThread().getName()+"aaaaaa");
		 }
		 int old=store;
		 store=store+count;
		 System.out.println(Thread.currentThread().getName()+"库存:"+old+"增加"+count+"后："+store);
		 substractCondition.signal();
		}catch(Exception e){
			
		}finally{
			lock.unlock();
		}
		
	}
	public void subtract(int count){
 		try{
		lock.lock();
		 if((store-count)<=0){
			 System.out.println(Thread.currentThread().getName()+"库存不足,等待生产");
			 substractCondition.await();
			 System.out.println(Thread.currentThread().getName()+"bbbbbb");
		 }
		 
		 int old=store;
		 store=store-count;
		 System.out.println(Thread.currentThread().getName()+"库存:"+old+"减少"+count+"后："+store);
		 addCondition.signal();
 		}catch(Exception e){
			
		}finally{
			lock.unlock();
		}
		
	}
	public int getStore(){
		return store;
	}
	
}
