package cn.eccto.study.sb.servejsp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * jsp 动态文件的 jar 包打包方式下的加载
 *
 * @author EricChen 2019/12/06 18:48
 */
@SpringBootApplication
public class ServeJspExample {
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ServeJspExample.class);
        builder.profiles("serve-jsp");
        builder.run(args);
    }

}
