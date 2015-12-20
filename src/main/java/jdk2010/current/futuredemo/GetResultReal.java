package jdk2010.current.futuredemo;

public class GetResultReal implements GetReult {

    
    @Override
    public String getResult() {
        System.out.println("====开始获取真实数据====");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("====结束获取真实数据====");
        return "abcReal";
    }

}
