package jdk2010.io.bio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BIODemo {
	
	public static String fromFile="c:\\eclipse-jee-luna-SR1-win32-x86_64.zip";
	
	public static String toFile="c:\\2.rar";
	
	
	//1.复制文件byInputStream
	public static void copyFileByInputStream() throws IOException{
		long begin=System.currentTimeMillis();
		InputStream in = new BufferedInputStream(new FileInputStream(fromFile));
		OutputStream out =new BufferedOutputStream(new FileOutputStream(toFile));
		int length=0;
		byte[] b=new byte[1024];
		while((length=in.read(b))!=-1){
			out.write(b, 0, length);
		}
		out.close();
		in.close();
		long end=System.currentTimeMillis();
		System.out.println("copyFileByInputStream:"+(end-begin));
		
	}
	
	//2.复制文件byFileWriter
		public static void copyFileByWriter() throws IOException{
			long begin=System.currentTimeMillis();
			 Reader reader=new BufferedReader(new FileReader(new File(fromFile)));
			 Writer writer=new BufferedWriter(new FileWriter(new File(toFile)));
			 char[] c=new char[1024];
			 int length=0;
 			 while((length=reader.read(c))!=-1){
				 writer.write(c, 0, length);
			 }
 			  
 			 writer.close();
 			 reader.close();
 			long end=System.currentTimeMillis();
 			System.out.println("copyFileByWriter:"+(end-begin));
		}
		//3.复制文件copyFileByWriterReadLine
				public static void copyFileByWriterReadLine() throws IOException{
					long begin=System.currentTimeMillis();
					BufferedReader reader=new BufferedReader(new FileReader(new File(fromFile)));
					BufferedWriter  writer=new BufferedWriter(new FileWriter(new File(toFile)));
					 String line="";
		 			 while((line=reader.readLine())!=null){
		 				writer.write(line);
		 				writer.flush();  
					 }
		 			 writer.close();
		 			 reader.close();
		 			long end=System.currentTimeMillis();
		 			System.out.println("copyFileByWriterReadLine:"+(end-begin));
				}
	
	
	public static void printFile() throws IOException{
		FileChannel channel=FileChannel.open(Paths.get("c:\\a.txt"),StandardOpenOption.READ);
		ByteBuffer buffer=ByteBuffer.allocate(1024);
		int length=0;
		while((length=channel.read(buffer))!=-1){
			buffer.clear();
			System.out.println(Charset.forName("gbk").decode(buffer).toString());
			buffer.flip();
		}
		channel.close();
	}
	
	public static void main(String[] args) throws IOException {
		copyFileByInputStream(); 
		copyFileByWriter();
		copyFileByWriterReadLine();
		//printFile();
//		copyFileByInputStream:562
//		copyFileByWriter:8580
//		copyFileByWriterReadLine:17527
//		copyByChannel:1966
//		copyByTransto:140
//		copyByTransFrom:156
//		copyByMapBufferChannel:2667
	}
}
