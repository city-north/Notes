package cn.eccto.study.springframework.tutorials.conditional.serivce.impl;

import cn.eccto.study.springframework.tutorials.conditional.serivce.LocaleService;

/**
 * description
 *
 * @author JonathanChen 2019/11/15 20:59
 */
public class LocaleChinaServiceImpl implements LocaleService {

    @Override
    public void sayHello() {
        System.out.println("你好");
    }
}
