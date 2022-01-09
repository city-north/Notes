package chapter02;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen
 */
public class StringTest {
    public static void main(String[] args) {
        String str1 = "abc".split(".");
        String str2= new String("abc");
        String str3= str2.intern();
        System.out.println(str1==str2);
        System.out.println(str2==str3);
        System.out.println(str1==str3);
    }


}
