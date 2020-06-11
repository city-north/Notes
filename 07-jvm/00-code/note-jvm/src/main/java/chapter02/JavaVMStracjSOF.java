package chapter02;

/**
 * <p>
 * -Xss160k
 * </p>
 *
 * @author EricChen
 */
public class JavaVMStracjSOF {

    private int stackLength =1;
    public void stackLeak(){
        stackLength ++;
        stackLeak();
    }

    public static void main(String[] args) throws Throwable {
        JavaVMStracjSOF oom = new JavaVMStracjSOF();
        try{
            oom.stackLeak();
        }catch (Throwable e){
            System.out.println("stack length:" + oom.stackLength);
            throw e;
        }
    }
}
