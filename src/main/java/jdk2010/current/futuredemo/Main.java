package jdk2010.current.futuredemo;

public class Main {
    public static void main(String[] args) {
        Client client = new Client();
        GetResultFuture future=client.getRequest();
        System.out.println("��ʼ����������");
        
        System.out.println(future.getResult());
        System.out.println("xxxx");
    }
}   
