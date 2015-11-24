package jdk2010.io.nio;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NIODemo {

	public static String fromFile="c:\\eclipse-jee-luna-SR1-win32-x86_64.zip";
	
	public static String toFile="c:\\6.zip";
	
	public static void delete() throws IOException{
		File file=new File(toFile);
		file.delete();
		file.createNewFile();
	}	
	
	// copyByCannel
	public static void copyByChannel() throws IOException {
		delete();
		long begin=System.currentTimeMillis();
		
		FileChannel inChannel=FileChannel.open(Paths.get(fromFile),StandardOpenOption.READ);
		FileChannel outChannel=FileChannel.open(Paths.get(toFile),StandardOpenOption.WRITE);
		
		ByteBuffer buffer=ByteBuffer.allocate(1024*1024);
		int length=0;
 		while((length=inChannel.read(buffer))!=-1){
 			buffer.clear();
			outChannel.write(buffer);
			buffer.flip();
		}
		outChannel.close();
		inChannel.close();
		long end=System.currentTimeMillis();
		System.out.println("copyByChannel:"+(end-begin));
	}
	
	// copyByCannel
		public static void copyByMapBufferChannel() throws IOException {
			delete();
			long begin=System.currentTimeMillis();
			
			FileChannel inChannel=FileChannel.open(Paths.get(fromFile),StandardOpenOption.READ);
			FileChannel outChannel=FileChannel.open(Paths.get(toFile),StandardOpenOption.WRITE);
			
			MappedByteBuffer mappedByteBuffer=inChannel.map(MapMode.READ_ONLY,0,inChannel.size());
			outChannel.write(mappedByteBuffer);
			outChannel.close();
			inChannel.close();
			long end=System.currentTimeMillis();
			System.out.println("copyByMapBufferChannel:"+(end-begin));
		}
	
	public static void copyByTransto() throws IOException{
		delete();
		long begin=System.currentTimeMillis();
		FileChannel inChannel=FileChannel.open(Paths.get(fromFile),StandardOpenOption.READ);
		FileChannel outChannel=FileChannel.open(Paths.get(toFile),StandardOpenOption.WRITE);
		inChannel.transferTo(0,inChannel.size(),outChannel);
		outChannel.close();
		inChannel.close();
		long end=System.currentTimeMillis();
		System.out.println("copyByTransto:"+(end-begin));
	}
	
	public static void copyByTransFrom() throws IOException{
		delete();
		long begin=System.currentTimeMillis();
		FileChannel inChannel=FileChannel.open(Paths.get(fromFile),StandardOpenOption.READ);
		FileChannel outChannel=FileChannel.open(Paths.get(toFile),StandardOpenOption.WRITE);
		outChannel.transferFrom(inChannel,0,inChannel.size());
		outChannel.close();
		inChannel.close();
		long end=System.currentTimeMillis();
		System.out.println("copyByTransFrom:"+(end-begin));
	}

	public static void main(String[] args) throws IOException {
		copyByChannel();
		copyByTransto();
		copyByTransFrom();
		copyByMapBufferChannel();
//		
//		copyByTransto:188
//		copyByTransFrom:171
//		copyByMapBufferChannel:2433
		
	}
}
