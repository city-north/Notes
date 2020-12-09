package cn.eccto.study.springframework.metadata;

import cn.eccto.study.springframework.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import java.util.Map;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/12/9 20:29
 */
@ImportResource("classpath:META-INF/dependency-lookup-context.xml")
@Import(User.class)
public class JavaAnnotationConfigDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(JavaAnnotationConfigDemo.class);

        applicationContext.refresh();

        Map<String, User> beansOfType = applicationContext.getBeansOfType(User.class);
        beansOfType.forEach((k,v) ->{
            System.out.printf("beanName : %s , bean: %s \n",k,v);
        });

        applicationContext.close();
    }
}
