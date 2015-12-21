package jdk2010.current.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class InterruptDemo {
    public static void listFile(String path) throws InterruptedException {
        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        File[] fileList = file.listFiles();
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("�ļ�ɨ�������ж�");
        }
        // if(true){
        // throw new RuntimeException("�ļ�ɨ�������ж�");
        // }
        if (fileList != null) {
            for (File secondFile : fileList) {
                System.out.println(secondFile.getAbsolutePath());
                listFile(secondFile.getAbsolutePath());
            }
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

    public static void main(String[] args) {
        final Thread listFileThread = new Thread(new Runnable() {
            public void run() {
                try {
                    InterruptDemo.listFile("d:\\");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        new Thread() {
            public void run() {
                while (true) {
                    if ("quit".equalsIgnoreCase(readFromConsole())) {
                        listFileThread.interrupt();
                    } else {
                        System.out.println("����quit�˳��ļ�ɨ��");
                    }
                }
            }
        }.start();
        listFileThread.start();
    }
}
