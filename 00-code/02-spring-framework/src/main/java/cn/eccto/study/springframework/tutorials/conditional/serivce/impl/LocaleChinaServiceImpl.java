package cn.eccto.study.springframework.tutorials.conditional.serivce.impl;

import cn.eccto.study.springframework.tutorials.conditional.serivce.LocaleService;

/**
 * description
 *
 * @author EricChen 2019/11/15 13:59
 */
public class LocaleChinaServiceImpl implements LocaleService {

    @Override
    public void sayHello() {
        System.out.println("你好");
    }
}
