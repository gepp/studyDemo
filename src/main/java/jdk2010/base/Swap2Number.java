package jdk2010.base;

public class Swap2Number {
    public static void main(String[] args) {
        int a=6;
        int b=3;
        a=a^b;
        System.out.println(a);
        b=b^a;
        System.out.println(b);
        a=a^b;
        System.out.println(a);
        System.out.println(b);
    }
}
