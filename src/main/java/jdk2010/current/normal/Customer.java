package jdk2010.current.normal;

import java.util.List;

public class Customer  extends Thread{
	List<Integer> list; 
	public Customer(List<Integer> list){
		this.list=list;
		start();
	}
	@Override
	public void run() {
		while(true){
			synchronized (list) {
				if(list.size()>0){
					System.out.println("������Ʒ���:"+(list.get(list.size()-1)));
					list.remove(list.size()-1);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					System.out.println("��Ʒ������,�ȴ�����");
					 
				}
			}
	
		}
	}
}
