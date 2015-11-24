package jdk2010.current.future;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FutureTest {
		public static void main(String[] args) throws InterruptedException, ExecutionException {
//			ExecutorService service1=Executors.newFixedThreadPool(5);
//			FutureTask<String> future=new FutureTask<String>(new FutureCallable());
//			Future<String> a=service1.submit(new FutureCallable());
//			Future<String> b=service1.submit(new FutureCallable2());
//			Future<String> c=  (Future<String>) service1.submit(future);
//			while(!a.isDone()){
//				System.out.println("等待a中");
//				Thread.sleep(1000);
//			}
//			Thread.sleep(6000);
//			System.out.println("begin-----");
//			System.out.println(future.get());
// 			String out=	a.get()+b.get();
//			
//			System.out.println("out:"+out);
			
			//****以下组装多线程 字符串相加
//			ExecutorService service1=Executors.newFixedThreadPool(5);
//			Future<String> numFuture;
//			ArrayList<Future<String>> list=new ArrayList<Future<String>>();
//			for(int i=1;i<=100;i++){
//				numFuture=service1.submit(new NumberFutureCallable(i+""));
//				list.add(numFuture);
//			}
//			System.out.println("list.size()"+list.size());
//			
//			String total="";
//			while(true){
//			Iterator<Future<String>> iterator=list.iterator();
//			while(iterator.hasNext()){
//				Future<String> f=iterator.next();
// 				if(f.isDone()){
//					System.out.println("f.get():"+f.get());
//					total=total+":"+f.get();
//					System.out.println("total:"+total);
//					iterator.remove();
//				}else{
// 					Thread.sleep(200);
//					System.out.println("未计算，剩余线程数:"+list.size());
//					continue;
// 				}
//			}
//			if(list.size()==0){
//				service1.shutdown();
//			}
//			}
			//****以下组装多线程 字符串相加
			ExecutorService service1=Executors.newFixedThreadPool(5);
			FpkjFutureTask numFuture;
			ArrayList<Future<String>> list=new ArrayList<Future<String>>();
			ArrayList<String> unokList=new ArrayList<String>();
			for(int i=1;i<=100;i++){
				numFuture=new FpkjFutureTask(new NumberFutureCallable(i+""));
				numFuture.setEncodeStr(i+"");
				service1.submit(numFuture);
				list.add(numFuture);
			}
			String total="";
			while(true){
			Iterator<Future<String>> iterator=list.iterator();
			while(iterator.hasNext()){
				FpkjFutureTask f=(FpkjFutureTask)iterator.next();
 				if(f.isDone()){
					System.out.println("f.get():"+f.get());
					total=total+":"+f.get();
					System.out.println("total:"+total);
					iterator.remove();
				}else{
  					String s="";
  					try{
  						s=f.get(5,TimeUnit.SECONDS);
  						total=total+":"+f.get();
  						
  					}catch(Exception e){
  						System.out.println("5秒超时，进入系统回收");
  						unokList.add(f.getEncodeStr());
  					}finally{
  						iterator.remove();
  					}
					System.out.println("未计算，剩余线程数:"+list.size());
					 
 				}
			}
			if(list.size()==0){
				
				service1.shutdown();
				System.out.println("未完成list---->");
				for(String s:unokList){
					System.out.println(s);
				}
				break;
			}
			}
			
		}
		 
}
