package org.maomao.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/6/2.
 */
public class testCountDownLatch {
    final static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws InterruptedException {
        java.util.concurrent.Executor executor = Executors.newFixedThreadPool(10);
        BlockingQueue q = new ArrayBlockingQueue(4);

        CountDownLatch latch=new CountDownLatch(2);//两个工人的协作
        TestRunnable worker1=new TestRunnable("zhang san", 1000, latch,q);
        TestRunnable worker2=new TestRunnable("li si", 2000, latch,q);
        TestRunnable worker3=new TestRunnable("zhang san", 1000, latch,q);
        TestRunnable worker4=new TestRunnable("zhang san", 3000, latch,q);

        worker1.start();//
        worker2.start();//
        worker3.start();//
        worker4.start();//
        latch.await();//等待所有工人完成工作
        System.out.println("all work done at "+sdf.format(new Date()));
        while (true){
            System.out.println(q.take());
        }
    }



}
