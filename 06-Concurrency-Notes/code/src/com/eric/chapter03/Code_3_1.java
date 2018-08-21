package com.eric.chapter03;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Code_3_1 {
    private static boolean ready = false;
    private static int number;

    private static class ReaderThread extends Thread{
        @Override
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }

    public static void main(String[] args){
        new ReaderThread().start();
        number = 42;
        ready = true;
    }

}

