package jdk2010.current.futuredemo;


public class Client {

    public GetResultFuture  getRequest() {
        final GetResultFuture future = new GetResultFuture();
        new Thread(new Runnable() {

            @Override
            public void run() {
                GetResultReal real = new GetResultReal();
                String s = real.getResult();
                future.setResult(s);

            }
        }).start();
        System.out.println("立即返回抽象的GetResultFuture");
        return future;
    }
}
