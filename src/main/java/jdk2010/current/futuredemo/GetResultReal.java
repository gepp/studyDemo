package jdk2010.current.futuredemo;

public class GetResultReal implements GetReult {

    
    @Override
    public String getResult() {
        System.out.println("====��ʼ��ȡ��ʵ����====");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("====������ȡ��ʵ����====");
        return "abcReal";
    }

}
