package cn.eccto.study.springframework.sourcecode;

import cn.eccto.study.springframework.constraints.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/09/17 19:37
 */
@Slf4j
public class ClassPathXmlApplicationContextExample {


    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("sourcecode/beanFactoryTest.xml");
        final Object student = applicationContext.getBean("student");
        final Student bean = applicationContext.getBean(Student.class);
        log.error("" + student);
        log.error("" + bean);
    }

}
