package cn.eccto.study.springframework.tutorials.databinder;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

/**
 * 使用 {@link DataBinder} 对 javaBean 进行的属性进行绑定
 *
 * @author JonathanChen 2019/11/21 20:04
 */
public class DataBinderExample {
    public static void main(String[] args) {

        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("anInt", "10");

        TestBean testBean = new TestBean();
        DataBinder db = new DataBinder(testBean);

        db.bind(mpv);
        System.out.println(testBean);

    }

    public static class TestBean {
        private int anInt;

        public int getAnInt() {
            return anInt;
        }

        public void setAnInt(int anInt) {
            this.anInt = anInt;
        }

        @Override
        public String toString() {
            return "TestBean{anInt=" + anInt + '}';
        }
    }
}
