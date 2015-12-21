package jdk2010.current.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class FileScanner {
    private static void listFile(File f) throws InterruptedException {
        if (f == null) {
            throw new IllegalArgumentException();
        }
        if (f.isFile()) {
            System.out.println(f);
            return;
        }
        File[] allFiles = f.listFiles();
        if (Thread.interrupted()) {
            throw new InterruptedException("�ļ�ɨ�������ж�");
        }
        for (File file : allFiles) {
            // �����Խ��жϼ��ŵ�����
            listFile(file);
        }
    }

    public static String readFromConsole() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            return reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) throws Exception {
        final Thread fileIteratorThread = new Thread() {
            public void run() {
                try {
                    listFile(new File("e:\\"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread() {
            public void run() {
                while (true) {
                    if ("quit".equalsIgnoreCase(readFromConsole())) {
                        if (fileIteratorThread.isAlive()) {
                            fileIteratorThread.interrupt();
                            return;
                        }
                    } else {
                        System.out.println("����quit�˳��ļ�ɨ��");
                    }
                }
            }
        }.start();
        fileIteratorThread.start();
    }
}
