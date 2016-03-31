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

    public static String fromFile = "c:\\test.rar";

    public static String toFile = "c:\\test1.rar";

    // 1.复制文件byInputStream
    public static void copyFileByInputStream() throws IOException {
        long begin = System.currentTimeMillis();
        InputStream in = new BufferedInputStream(new FileInputStream(fromFile));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(toFile));
        int length = 0;
        byte[] b = new byte[1024];
        while ((length = in.read(b)) != -1) {
            out.write(b, 0, length);
        }
        out.close();
        in.close();
        long end = System.currentTimeMillis();
        System.out.println("copyFileByInputStream:" + (end - begin));

    }

    // 2.复制文件byFileWriter
    public static void copyFileByWriter() throws IOException {
        long begin = System.currentTimeMillis();
        Reader reader = new BufferedReader(new FileReader(new File(fromFile)));
        Writer writer = new BufferedWriter(new FileWriter(new File(toFile)));
        char[] c = new char[1024];
        int length = 0;
        while ((length = reader.read(c)) != -1) {
            writer.write(c, 0, length);
        }

        writer.close();
        reader.close();
        long end = System.currentTimeMillis();
        System.out.println("copyFileByWriter:" + (end - begin));
    }

    // 3.复制文件copyFileByWriterReadLine
    public static void copyFileByWriterReadLine() throws IOException {
        long begin = System.currentTimeMillis();
        BufferedReader reader = new BufferedReader(new FileReader(new File(fromFile)));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(toFile)));
        String line = "";
        while ((line = reader.readLine()) != null) {
            writer.write(line);
            writer.flush();
        }
        writer.close();
        reader.close();
        long end = System.currentTimeMillis();
        System.out.println("copyFileByWriterReadLine:" + (end - begin));
    }

    public static void printFile() throws IOException {
        FileChannel channel = FileChannel.open(Paths.get("c:\\a.txt"), StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int length = 0;
        while ((length = channel.read(buffer)) != -1) {
            buffer.clear();
            System.out.println(Charset.forName("gbk").decode(buffer).toString());
            buffer.flip();
        }
        channel.close();
    }

    public static void copyByTransto() throws IOException {
        FileChannel fcInput = FileChannel.open(Paths.get(fromFile), StandardOpenOption.READ);
        ByteBuffer byteBuf = ByteBuffer.allocate(1024);
        long timeStar = System.currentTimeMillis();// 得到当前的时间
        fcInput.read(byteBuf);// 1 读取

        FileChannel fcOutput = FileChannel.open(Paths.get(toFile), StandardOpenOption.WRITE);

        fcInput.transferTo(0, fcInput.size(), fcOutput);

        long timeEnd = System.currentTimeMillis();// 得到当前的时间

        System.out.println("Write time :" + (timeEnd - timeStar) + "ms");
        fcInput.close();
    }

    public static void main(String[] args) throws IOException {
        // copyFileByInputStream();
        // copyFileByWriter();
        // copyFileByWriterReadLine();
        // printFile();
        // copyFileByInputStream:562
        // copyFileByWriter:8580
        // copyFileByWriterReadLine:17527
        // copyByChannel:1966
        // copyByTransto:140
        // copyByTransFrom:156
        // copyByMapBufferChannel:2667
        // copyFileByWriter();
        copyByTransto();
    }
}
