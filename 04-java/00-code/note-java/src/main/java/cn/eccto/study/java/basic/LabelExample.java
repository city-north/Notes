package cn.eccto.study.java.basic;

/**
 * Example of Label Statement
 *
 * @author Jonathan 2020/01/14 17:08
 */
public class LabelExample {
    public static void main(String[] args) {
        outer: for (int i = 0; i < 10; i++) {
            inner: for (int j = 10; j > 0; j--) {
                if (i != j) {
                    System.out.println(i);
                    break outer;
                }else{
                    System.out.println("-->>" + i);
                    continue inner;
                }
            }
        }
    }

}
