package cn.eccto.study.springframework.tutorials.quick;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * description
 *
 * @author JonathanChen 2019/11/05 22:23
 */
public class HelloWorldServiceClient {

    @Autowired
    private HelloWorldService helloWorld;

    public void showMessage() {
        helloWorld.sayHi("Hello world!");
    }
}