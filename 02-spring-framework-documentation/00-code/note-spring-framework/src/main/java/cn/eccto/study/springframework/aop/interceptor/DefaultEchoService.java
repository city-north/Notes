package cn.eccto.study.springframework.aop.interceptor;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen
 */
public class DefaultEchoService implements EchoService {


    @Override
    public String echo(String message) throws IllegalStateException {
        return "EHCO : " + message;
    }
}
