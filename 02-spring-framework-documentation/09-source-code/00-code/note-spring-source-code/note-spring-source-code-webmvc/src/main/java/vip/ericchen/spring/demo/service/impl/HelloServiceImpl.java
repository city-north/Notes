package vip.ericchen.spring.demo.service.impl;

import vip.ericchen.spring.annotation.Controller;
import vip.ericchen.spring.annotation.Service;
import vip.ericchen.spring.demo.service.IHelloService;

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
