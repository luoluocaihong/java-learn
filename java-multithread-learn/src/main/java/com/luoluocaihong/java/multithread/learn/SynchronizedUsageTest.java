package com.luoluocaihong.java.multithread.learn;

/**
 * Created by xh on 2019/3/13.
 * Synchronization
 * https://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html#jls-17.1
 */
public class SynchronizedUsageTest {
    private static SynchronizedUsageTest resouce = new SynchronizedUsageTest();
    private int count = 0;
    private static int index = 0;

    public static void main(String[] args) {

        Thread threadA = new Thread(() -> {
            resouce.testSynchronizedblock();
            resouce.testSynchronizedblock1();
            resouce.testSynchronizedMethod();
            SynchronizedUsageTest.testSynchronizedMethod1();
        });
        threadA.setName("ThreadA");

        Thread threadB = new Thread(() -> {
            resouce.testSynchronizedblock();
            resouce.testSynchronizedblock1();
            resouce.testSynchronizedMethod();
            SynchronizedUsageTest.testSynchronizedMethod1();
        });
        threadB.setName("ThreadB");

        threadA.start();
        threadB.start();

        threadA.interrupt();
    }


    //测试Synchronized作用于代码块
    public void testSynchronizedblock() {
        synchronized (SynchronizedUsageTest.resouce) {
            System.out.println(Thread.currentThread().getName() + " testSynchronizedblock,count is " + (++count));
            int x=3/0;
        }
    }
    //测试Synchronized作用于代码块
    public void testSynchronizedblock1() {
        synchronized (SynchronizedUsageTest.class) {
            System.out.println(Thread.currentThread().getName() + " testSynchronizedblock1,index is " + (++index));
        }
    }



    //测试Synchronized作用于方法
    public synchronized void testSynchronizedMethod() {
        System.out.println(Thread.currentThread().getName() + " testSynchronizedMethod,count is " + (++count));
    }
    //测试Synchronized作用于方法
    public synchronized static void testSynchronizedMethod1() {
        System.out.println(Thread.currentThread().getName() + " testSynchronizedMethod1,index is " + (++index));
    }
}
