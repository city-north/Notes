package cn.eccto.study.java.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DefaultUserService implements IUserService  {
    @Override
    public User getUser(String id) {
        System.out.println("start getUser");
        User eric = new User().setAge(10).setName("eric");
        System.out.println("end getUser");
        return eric;
    }

}
