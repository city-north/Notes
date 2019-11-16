package cn.eccto.study.springframework.tutorials;

import cn.eccto.study.springframework.tutorials.scope.Config;
import cn.eccto.study.springframework.tutorials.scope.UserInfo;
import cn.eccto.study.springframework.tutorials.scope.UserRegistrationBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;
import java.util.Scanner;

/**
 * description
 *
 * @author EricChen 2019/11/14 15:05
 */
public class ScopeExample {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);

        UserRegistrationBean registrationBean = context.getBean(UserRegistrationBean.class);

        while (true) {
            System.out.printf("[registration bean instance: %s]\n", System.identityHashCode(registrationBean));
            System.out.println("Enter new user. Enter exit to terminate");
            registerUser(registrationBean);
            registrationBean = context.getBean(UserRegistrationBean.class);
        }
    }

    private static void registerUser(UserRegistrationBean registrationBean) {

        UserInfo userInfo = new UserInfo();
        registrationBean.setUserInfo(userInfo);

        Map<String, String> errors = null;

        while (errors == null || errors.size() > 0) {
            if (errors != null) {
                System.out.println("Errors : " + errors.values() + "\n");
                System.out.println("Please enter exit to terminate");
            }

            if (errors == null || errors.containsKey(UserRegistrationBean.KEY_EMAIL)) {
                userInfo.setEmail(getUserInput("Enter Email"));
            }
            if (errors == null || errors.containsKey(UserRegistrationBean.KEY_PASSWORD)) {
                userInfo.setPassword(getUserInput("Enter Password"));
            }

            errors = registrationBean.validate();
        }
        registrationBean.register();
    }

    public static String getUserInput(String instruction) {
        System.out.print(instruction + ">");
        String s = scanner.nextLine();
        if ("exit".equals(s)) {
            System.exit(0);
        }
        return s;

    }
}
