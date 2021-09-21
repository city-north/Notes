package cn.eccto.study.springframework.tutorials.quick;

/**
 * description
 *
 * @author JonathanChen 2019/11/05 22:23
 */
public class HelloWorldServiceImpl implements HelloWorldService {

    public void sayHi(String message) {
        System.out.println(message);
    }
}
