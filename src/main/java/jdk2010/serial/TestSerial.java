package jdk2010.serial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TestSerial {
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		StudentSerial s1=new StudentSerial();
					  s1.setAge(18);
					  s1.setName("ykk");
		 File f1 = new File("s1.out");		
		 ObjectInputStream in = null;
		 ObjectOutputStream out = null;
		 out = new ObjectOutputStream(new FileOutputStream(f1));
		 out.writeObject(s1);
		 out.flush();
		 System.out.println("第一次写入："+f1.length());
		 out.writeObject(s1);
		 out.flush();
		 System.out.println("第二次写入："+f1.length());
		 out.close();
		 in = new ObjectInputStream(new FileInputStream(f1));
		 Object obj = in.readObject();
		 Object obj2 =in.readObject();
		 System.out.println(obj == obj2);
	}
}
