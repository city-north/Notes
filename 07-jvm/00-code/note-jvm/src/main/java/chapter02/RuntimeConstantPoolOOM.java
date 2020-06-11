package chapter02;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * -XX:PermSize=6M -XX:MaxPermSize=6M
 * </p>
 *
 * @author EricChen
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        Set<String> set =  new HashSet<String>();
        short  i = 0;
        while (true){
            set.add(String.valueOf(i++).intern());
        }
    }
}
