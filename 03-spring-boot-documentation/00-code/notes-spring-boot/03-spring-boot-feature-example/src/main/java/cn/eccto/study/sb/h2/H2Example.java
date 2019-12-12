package cn.eccto.study.sb.h2;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * description
 *
 * @author EricChen 2019/12/12 11:25
 */
@SpringBootApplication
public class H2Example {
    public static void main(String[] args) {
        ConfigurableApplicationContext h2 = new SpringApplicationBuilder(H2Example.class)
                .web(WebApplicationType.SERVLET)
                .profiles("h2")
                .run(args);
        JdbcTemplatePersonDao dao = h2.getBean(JdbcTemplatePersonDao.class);
        List<Person> people = dao.loadAll();
        System.out.println(people);
        Person build = Person.builder().address("测试").firstName("Eric").lastName("Chen").build();
        dao.save(build);
        people = dao.loadAll();
        System.out.println(people);
    }

}
