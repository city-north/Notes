package vip.ericchen.spring.demo.service.impl;

import vip.ericchen.spring.demo.service.IHelloService;
import vip.ericchen.study.spring.framework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Override
    public String queryUser(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        String json = "{name:\"" + name + "\",time:\"" + time + "\"}";
        return json;
    }
}
