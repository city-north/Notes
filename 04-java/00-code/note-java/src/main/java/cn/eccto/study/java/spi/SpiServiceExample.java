package cn.eccto.study.java.spi;

import java.util.ServiceLoader;

/**
 * description
 *
 * @author Jonathan 2019/11/03 21:08
 */
public class SpiServiceExample {

    public static void main(String[] args) {
        ServiceLoader<MyService> shouts = ServiceLoader.load(MyService.class);
        for (MyService s : shouts) {
            s.service();
        }
    }




}
