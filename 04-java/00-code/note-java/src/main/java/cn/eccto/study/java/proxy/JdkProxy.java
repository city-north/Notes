package cn.eccto.study.java.proxy;

public class JdkProxy {
    public static void main(String[] args) {
        //生成的代理类保存到磁盘
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");


        IUserService userService  = new DefaultUserService();
        ProxyFactory proxyFactory = new ProxyFactory(IUserService.class);
        MyProxy<IUserService> myProxy = new MyProxy(userService);
        IUserService proxy = (IUserService) proxyFactory.getProxy(myProxy);
        User user = proxy.getUser("123");
        System.out.println(user);
    }
}
