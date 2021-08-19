package cn.eccto.study.java.jvm;

/**
 * <p>
 * 栈溢出错误实例 \VM Args = -Xss160k
 * 无论是栈帧太大还是虚拟机栈栈容量太小,都会抛出 StackOverflowError
 * </p>
 *
 * @author Jonathan 2020/05/02 17:20
 */
public class ExampleOfStackOverFlowError {

    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        ExampleOfStackOverFlowError stackOverFlowError = new ExampleOfStackOverFlowError();
        try {
            stackOverFlowError.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length:"+ stackOverFlowError.stackLength);
            throw e;
        }
    }

}
