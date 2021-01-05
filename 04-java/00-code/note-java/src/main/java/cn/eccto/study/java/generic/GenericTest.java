package cn.eccto.study.java.generic;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2021/01/05 20:34
 */
public class GenericTest {


    public static <T> Pair<T> makerArrayList(Class<T> c) throws IllegalAccessException, InstantiationException {
        return new Pair<>(c.newInstance(), c.newInstance());
    }


    public static class Pair<T> {

        private T left;
        private T right;

        public Pair(T left, T right) {
            this.left = left;
            this.right = right;
        }
    }
}
