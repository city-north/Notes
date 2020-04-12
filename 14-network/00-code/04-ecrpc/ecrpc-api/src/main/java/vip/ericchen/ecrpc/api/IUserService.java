package vip.ericchen.ecrpc.api;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 21:15
 */
public interface IUserService {

    User getUser();

    boolean saveUser(User user);
}
