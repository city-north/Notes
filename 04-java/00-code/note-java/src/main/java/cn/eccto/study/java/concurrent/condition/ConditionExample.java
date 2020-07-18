package cn.eccto.study.java.concurrent.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionExample {
    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        reentrantLock.lock();

        try{
            System.out.println("begin wait");
            condition.await();
            System.out.println("end Wait");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            reentrantLock.unlock();
        }

        reentrantLock.lock();

        try{
            System.out.println("begin singal");
            condition.signal();
            System.out.println("end singal");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            reentrantLock.unlock();
        }


    }
}
