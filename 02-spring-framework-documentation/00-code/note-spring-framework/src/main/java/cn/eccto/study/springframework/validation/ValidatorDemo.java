package cn.eccto.study.springframework.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <p>
 * A demo for {@link org.springframework.validation.Validator}
 * </p>
 *
 * @author JonathanChen
 */
public class ValidatorDemo {

    public static void main(String[] args) {

        Validator validator = new UserValidator();

        if (validator.supports(User.class)) {

        }
    }


    static class UserValidator implements Validator {

        @Override
        public boolean supports(Class<?> clazz) {
            return false;
        }

        @Override
        public void validate(Object target, Errors errors) {

        }
    }

    static class User {
        int age;
        String name;

    }
}
