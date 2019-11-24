package cn.eccto.study.springframework.tutorials.methodValidation;

import cn.eccto.study.springframework.formatter.configuration.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * 方法校验
 *
 * @author EricChen 2019/11/22 22:56
 */
@Configuration
@ComponentScan("cn.eccto.study.springframework.tutorials.methodValidation")
public class MethodValidationExample {

    @Bean
    public Validator validatorFactory() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }



    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(MethodValidationExample.class);

        ReportTask task = context.getBean(ReportTask.class);
        System.out.println("-- calling ReportTask#createReport() --");
        System.out.println("-- with invalid parameters --");
        try {
            String status = task.createReport("", LocalDateTime.now().minusMinutes(30));
            System.out.println(status);
        } catch (ConstraintViolationException e) {
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage());
            }
        }
        System.out.println("-- with valid parameters but invalid return value --");
        try {
            String status = task.createReport("create reports", LocalDateTime.now().plusMinutes(30));
            System.out.println(status);
        } catch (ConstraintViolationException e) {
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage());
            }
        }

        UserTask userTask = context.getBean(UserTask.class);
        System.out.println("-- calling UserTask#registerUser() --");
        User user = new User();
        user.setEmail("tony-example.com");
        try {
            userTask.registerUser(user);
        } catch (ConstraintViolationException e) {
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                System.out.println(violation.getPropertyPath() + " " + violation.getMessage());
            }
        }
    }

    public static class User {
        @NotEmpty
        private String name;
        @Email
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }
}
