package cn.eccto.study.springframework.ioc;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author JonathanChen 2020/11/01 12:56
 */
public class HierarchicalBeanFactoryExample {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext parent = new ClassPathXmlApplicationContext("classpath:tutorials/hierarchical/dependency-parent.xml");
        ClassPathXmlApplicationContext child = new ClassPathXmlApplicationContext("classpath:tutorials/hierarchical/dependency-child.xml");
        child.setParent(parent);
        final Map<String, Apple> beansOfType = child.getBeansOfType(Apple.class);
        System.out.println("Apple in child :");//Apple{name='child', age=25}
        beansOfType.forEach((k, v) -> System.out.println(v));
        final Map<String, Apple> beansOfType1 = parent.getBeansOfType(Apple.class);
        System.out.println("Apple in Parent :");
        beansOfType1.forEach((k, v) -> System.out.println(v)); //Apple{name='parent', age=25}
        final Map<String, Apple> stringAppleMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(child, Apple.class);
        System.out.println("Apple use BeanFactoryUtils :");
        stringAppleMap.forEach((k, v) -> System.out.println(v)); //全查出来了


    }

}

