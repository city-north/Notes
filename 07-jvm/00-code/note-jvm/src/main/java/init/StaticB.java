package init;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen
 */
public class StaticB {

    static {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //在StaticB静态代码块里初始化类StaticA
            Class.forName("init.StaticA");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("staticB init OK");
    }
}
