package cn.eccto.study.springframework.tutorials.methodValidation;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * description
 *
 * @author EricChen 2019/11/22 22:57
 */
@Component
@Validated
public class UserTask {

    public void registerUser(@NotNull @Valid MethodValidationExample.User user){
        System.out.println("registering user: "+ user);
    }
}