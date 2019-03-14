package com.luoluocaihong.java.multithread.learn;

/**
 * Created by xh on 2019/3/13.
 *  Wait Sets and Notification
 *  https://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html#jls-17.2
 */
public class WaitNotifyTest {

    private static final Object monitorObj = new Object();
    private static boolean flag = false;
    private  boolean instanceFlag = false;

    public static void main(String[] args) throws InterruptedException {

        WaitNotifyTest test = new WaitNotifyTest();
        test.testInterruptWhenWait();

    }


    //验证场景:当一个线程wait状态时另一个线程对其中断,验证什么时候抛线程中断异常,什么时候清空线程中断状态
    //线程A对test对象加锁,判断条件instanceFlag不为true,wait;
    //线程B sleep 5s之后,将线程A中断,并对test对象加锁
    //线程A被中断后状态变为中断状态,返回await方法; 因为test对象被线程B锁住了,线程A阻塞；
    //此时打印线程A的状态为中断状态,线程B在同步块中修改instanceFlag=true,sleep 5s
    //线程B同步块执行完成之后,线程A获得test对象锁,此时抛出了中断异常,线程A中断状态被清空
    public void testInterruptWhenWait() {
        WaitNotifyTest test = new WaitNotifyTest();
        test.instanceFlag = false;
        Thread threadA = new Thread(() -> {
            synchronized (test) {
                while (!test.instanceFlag) {
                    try {
                        System.out.println("testInterruptBeforeWait.ThreadA wait before,current time:" + System.currentTimeMillis());
                        test.wait();
                        System.out.println("testInterruptBeforeWait.ThreadA wait after,current time:" + System.currentTimeMillis() + "isInterrupted:" + Thread.currentThread().isInterrupted());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("testInterruptBeforeWait.ThreadA isInterrupted:" + Thread.currentThread().isInterrupted() + ",time:" + System.currentTimeMillis());
            }

        });

        Thread threadB = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("testInterruptBeforeWait.ThreadB Begin: Sleep 5s");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //中断线程A
                threadA.interrupt();

                synchronized (test) {
                    try {
                        //打印线程A的中断状态
                        System.out.println("ThreadA isInterrupted =  " + threadA.isInterrupted());
                        test.instanceFlag = true;
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("testInterruptBeforeWait.ThreadB:" + System.currentTimeMillis());
            }
        });

        threadA.start();
        threadB.start();

        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("method END,ThreadA isInterrupted: " + threadA.isInterrupted());
    }
}
