package cn.eccto.study.java.spi.impl;

import cn.eccto.study.java.spi.MyService;

/**
 * description
 *
 * @author EricChen 2019/11/03 21:05
 */
public class DogService implements MyService {

    @Override
    public void service() {
        System.out.println("dog service");
    }
}
