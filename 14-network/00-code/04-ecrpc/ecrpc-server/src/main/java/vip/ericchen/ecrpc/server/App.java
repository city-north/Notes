package vip.ericchen.ecrpc.server;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 22:10
 */
public class App {
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer(9090);
        rpcServer.publish();
    }
}
