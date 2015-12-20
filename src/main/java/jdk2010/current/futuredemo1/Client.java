package jdk2010.current.futuredemo1;

public class Client {
    public GetResultFuture getResult() {
        final GetResultFuture future = new GetResultFuture();

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetResultReal real = new GetResultReal();
                future.setResult(real);
            }
        }).start();

        return future;
    }
}
