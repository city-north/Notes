package cn.eccto.study.java.generic;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2021/01/06 18:09
 */
public class Problem<T> {


    public static <T extends Throwable> void doWork(T t) throws T {
        try {

        } catch (Throwable e) {
            t.initCause(e);
            throw t;
        }
    }
}
