package gateway.filter;

public class PermissionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PermissionException(String msg) {
        super("这是一个自定义的权限错误" + msg);
    }
}
