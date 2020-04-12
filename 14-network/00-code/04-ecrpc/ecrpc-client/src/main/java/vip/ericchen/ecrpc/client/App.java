package vip.ericchen.ecrpc.client;

import vip.ericchen.ecrpc.api.IUserService;
import vip.ericchen.ecrpc.api.RpcProxyClient;
import vip.ericchen.ecrpc.api.User;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 21:59
 */
public class App {

    public static void main(String[] args) {
        IUserService localhost = RpcProxyClient.clientProxy(IUserService.class, "localhost", 9090);
        User user = localhost.getUser();
        System.out.println(user);

    }
}
