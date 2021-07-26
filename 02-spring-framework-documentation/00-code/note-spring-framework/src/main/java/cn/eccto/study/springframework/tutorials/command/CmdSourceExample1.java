package cn.eccto.study.springframework.tutorials.command;

import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.util.Arrays;

/**
 * 不使用 SpringContext 的情况下使用{@link SimpleCommandLinePropertySource}
 *
 * @author JonathanChen 2019/11/16 22:01
 */
public class CmdSourceExample1 {
    public static void main(String[] args) {
        SimpleCommandLinePropertySource ps = new SimpleCommandLinePropertySource(args);
        Arrays.stream(ps.getPropertyNames()).forEach(s ->
                System.out.printf("%s => %s%n", s, ps.getProperty(s)));
    }
}
