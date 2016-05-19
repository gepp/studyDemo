package jdk2010.base;

public class BaseClass {
    public static String printNum(int num) {
        String binaryStr = Integer.toBinaryString(num);
        if (binaryStr.length() == 32) {
            System.out.println(binaryStr);
        } else {
            int length=32 - binaryStr.length();
            for (int i = 0; i < length; i++) {
                binaryStr = "0" + binaryStr;
            }
            System.out.println(binaryStr);
        }
        return binaryStr;
    }
    
    public static int toNum(String binaryString){
        int num=Integer.parseInt(binaryString,2);
        System.out.println(num);
        return num;
    }
    
    
    
    public static void main(String[] args) {
        printNum(5);
        printNum(2);
        printNum(7);
        System.out.println(5^7);
        int num1 = 2;
        int num2 = 5;
        num1 = num1^num2;
        System.out.println(num1);
        System.out.println(num2);
        System.out.println("======");
        num2 = num2^num1;
        System.out.println(num1);
        System.out.println(num2);
        System.out.println("======");
        num1 = num1^num2;
        System.out.println(num1);
        System.out.println(num2);
        System.out.println("======");
        System.out.println("num1:" + num1 +"\n"+ "num2:" + num2 );
        int b=-1;
        System.out.println(( 0xff));
    }
}
