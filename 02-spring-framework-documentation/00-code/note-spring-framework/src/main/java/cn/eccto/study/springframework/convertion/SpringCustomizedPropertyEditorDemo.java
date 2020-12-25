package cn.eccto.study.springframework.convertion;

import cn.eccto.study.springframework.User;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/12/25 15:35
 */
public class SpringCustomizedPropertyEditorDemo {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/property-editors-context.xml");
        User user = applicationContext.getBean("user", User.class);
        System.out.println(user);
    }
}
