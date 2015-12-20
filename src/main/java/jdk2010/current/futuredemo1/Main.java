package jdk2010.current.futuredemo1;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Client client=new Client();
        GetResultFuture future=client.getResult();
        System.out.println("继续其他事情");
        String returnstr=future.getResult();
        System.out.println("结束");
    }
}
