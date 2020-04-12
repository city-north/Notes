package vip.ericchen.ecrpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ericchen.ecrpc.anno.RpcService;
import vip.ericchen.ecrpc.api.IUserService;
import vip.ericchen.ecrpc.api.User;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 21:17
 */
@RpcService(service = IUserService.class)
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public User getUser() {
        System.out.println("调用 vip.ericchen.ecrpc.server.UserServiceImpl ");
        User user = new User();
        user.setAge(11);
        user.setName("ericchen");
        return user;
    }

    public boolean saveUser(User user) {
        System.out.println("调用 vip.ericchen.ecrpc.server.UserServiceImpl.saveUser");
        return true;
    }
}
