package jdk2010.aspectj;

public aspect MyAspectJDemo {
    /**
     * �����е�,��־��¼�е�
     */
    pointcut recordLog():call(* HelloWord.sayHello(..));

    /**
     * �����е�,Ȩ����֤(ʵ�ʿ�������־��Ȩ��һ�����ڲ�ͬ��������,�����Ϊ������ʾ)
     */
    pointcut authCheck():call(* HelloWord.sayHello(..));

    /**
     * ����ǰ��֪ͨ!
     */
    before():authCheck(){
        System.out.println("sayHello����ִ��ǰ��֤Ȩ��");
    }

    /**
     * �������֪ͨ
     */
    after():recordLog(){
        System.out.println("sayHello����ִ�к��¼��־");
    }
}