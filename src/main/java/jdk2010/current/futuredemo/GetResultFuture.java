package jdk2010.current.futuredemo;

public class GetResultFuture implements GetReult {

    boolean isOK = false;
    public String realStr;

    public synchronized String getResult() {
        while (!isOK) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        return realStr;
    }

    public synchronized void setResult(String realStr) {
        this.realStr = realStr;
        isOK = true;
        notifyAll();
    }

}
