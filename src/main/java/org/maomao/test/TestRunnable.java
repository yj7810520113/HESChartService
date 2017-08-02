package org.maomao.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2017/6/2.
 */
public class TestRunnable extends Thread{
    final static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String workerName;
    int workTime;
    CountDownLatch latch;
    BlockingQueue<Object> q;
    public TestRunnable(String workerName ,int workTime ,CountDownLatch latch,BlockingQueue q){
        this.q=q;
        this.workerName=workerName;
        this.workTime=workTime;
        this.latch=latch;
    }
    public void run(){
//            System.out.println("Worker "+workerName+" do work begin at "+sdf.format(new Date()));
        try {
            q.put("Worker "+workerName+" do work begin at "+sdf.format(new Date()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doWork();//工作了
//            System.out.println("Worker "+workerName+" do work complete at "+sdf.format(new Date()));

        latch.countDown();//工人完成工作，计数器减一

    }

    private void doWork(){
        try {
            Thread.sleep(workTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
