package jdk2010.current.futuredemo1;

public class GetResultFuture {
    GetResultReal real = new GetResultReal();
    boolean isOk = false;

    public String getResult() throws InterruptedException {
        synchronized (this) {
            if (!isOk) {
                wait();
            }
            return real.getResult();
        }

    }

    public void setResult(GetResultReal real) {
        synchronized (this) {
            this.real = real;
            isOk = true;
            notify();
        }

    }
}
