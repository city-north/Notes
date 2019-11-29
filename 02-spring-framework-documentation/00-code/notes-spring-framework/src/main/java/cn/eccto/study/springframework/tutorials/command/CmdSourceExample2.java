package cn.eccto.study.springframework.tutorials.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

/**
 * 使用 SpringContext 的情况下使用{@link SimpleCommandLinePropertySource}
 *
 * @author EricChen 2019/11/16 22:06
 */
@Configuration
public class CmdSourceExample2 {

    @Bean
    public MyBean myBean1() {
        return new MyBean();
    }

    public static void main(String[] args) {

        PropertySource theSource = new SimpleCommandLinePropertySource(args);

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(CmdSourceExample2.class);

        context.getEnvironment().getPropertySources().addFirst(theSource);

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }


    public class MyBean {
        @Autowired
        private Environment environment;

        public void doSomething() {
            String value = environment.getProperty("myProp");
            System.out.println("the value of myProp: " + value);
        }
    }
}