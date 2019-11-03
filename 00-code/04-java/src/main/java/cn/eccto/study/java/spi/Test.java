package cn.eccto.study.java;

import cn.eccto.study.java.spi.MyService;

import java.util.ServiceLoader;

/**
 * description
 *
 * @author ec 2019/11/03 21:08
 */
public class Test {

    public static void main(String[] args) {
        ServiceLoader<MyService> shouts = ServiceLoader.load(MyService.class);
        for (MyService s : shouts) {
            s.service();
        }
    }

}
