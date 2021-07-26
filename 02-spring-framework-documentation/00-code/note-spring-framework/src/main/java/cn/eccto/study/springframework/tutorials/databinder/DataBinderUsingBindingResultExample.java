package cn.eccto.study.springframework.tutorials.databinder;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

/**
 * 本例介绍如何使用 {@link BindingResult},以及 {@link BeanWrapper} 和 {@link DataBinder} 的异常
 *
 * @author JonathanChen 2019/11/21 20:14
 * @see #bindWithBeanBinder DataBinder 在进行数据绑定时,如果类型不匹配,不会进行实际的转换,所以不会抛出异常
 * @see #bindWithBeanWapper BeanWrapper 在进行数据绑定时,如果类型不匹配,不会进行实际的转换,所以不会抛出异常TypeMismatchException
 */
public class DataBinderUsingBindingResultExample {
    public static void main(String[] args) {
        bindWithBeanBinder();
//        bindWithBeanWapper();
    }


    /**
     * DataBinder 在进行数据绑定时,如果类型不匹配,不会进行实际的转换,所以不会抛出异常
     */
    public static void bindWithBeanBinder() {
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("anInt", "10x"); // 10x cannot be converted to int type

        TestBean testBean = new TestBean();
        DataBinder db = new DataBinder(testBean);

        db.bind(mpv);
        db.getBindingResult().getAllErrors().stream().forEach(System.out::println);
        System.out.println(testBean);
    }


    /**
     * BeanWrapper 在进行数据绑定时进行实际的转换,当类型不匹配时会抛出异常 {@link TypeMismatchException}
     */
    public static void bindWithBeanWapper() {
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("anInt", "10x"); // 10x cannot be converted to int type

        BeanWrapper bw = new BeanWrapperImpl(new TestBean());
        bw.setPropertyValues(mpv);
        System.out.println(bw.getWrappedInstance());
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
