package chapter02.proxy.cglibProxy;

import chapter02.proxy.common.UserService;
import chapter02.proxy.common.UserServiceImpl;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Test {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        UserServiceCglibProxyFactory proxy = new UserServiceCglibProxyFactory();
        UserService user = proxy.createProxy(userService.getClass());
        user.save();

    }
}
