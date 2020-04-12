package vip.ericchen.ecrpc.api;

import java.io.Serializable;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 21:36
 */
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -2868693311624751282L;

    public RpcRequest(String className, String methodName, Object[] parameter) {
        this.className = className;
        this.methodName = methodName;
        this.parameter = parameter;
    }

    private String className;

    private String methodName;

    private Object[] parameter;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameter() {
        return parameter;
    }

    public void setParameter(Object[] parameter) {
        this.parameter = parameter;
    }
}
