package vip.ericchen.study.spring.framework.example.service.impl;


import vip.ericchen.study.spring.framework.example.service.IHelloService;
import vip.ericchen.study.spring.framework.stereotype.Service;

/**
 * description
 *
 * @author EricChen 2020/01/04 22:19
 */
@Service
public class HelloServiceImpl implements IHelloService {

    @Override
    public String hello() {
        return "Hello World EricChen";
    }
}
