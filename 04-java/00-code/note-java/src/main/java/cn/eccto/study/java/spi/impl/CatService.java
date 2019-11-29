package cn.eccto.study.java.spi.impl;

import cn.eccto.study.java.spi.MyService;

/**
 * description
 *
 * @author EricChen 2019/11/03 21:05
 */
class CatService implements MyService {

    @Override
    public void service() {
        System.out.println("cat service");
    }
}
