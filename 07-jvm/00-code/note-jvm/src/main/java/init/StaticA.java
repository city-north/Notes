package init;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen
 */
public class StaticA {

    static {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //在StaticA静态代码块里初始化类StaticB
            Class.forName("init.StaticB");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("staticA init OK");
    }

}
