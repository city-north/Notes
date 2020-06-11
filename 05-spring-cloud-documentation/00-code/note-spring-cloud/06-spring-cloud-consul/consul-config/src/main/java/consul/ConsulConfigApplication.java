package consul;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/06/11 19:11
 */
@SpringBootApplication
@RestController
@RefreshScope
public class ConsulConfigApplication {
    @Autowired
    private DiscoveryClient discoveryClient;



    public static void main(String[] args) {
        SpringApplication.run(ConsulConfigApplication.class,args);
    }


    @GetMapping("/services")
    public List<String> getService(){
        return discoveryClient.getServices();
    }

    // 读取远程配置
    @Value("${foo.bar.name}")
    private String name;

    // 将配置展示在页面
    @GetMapping("/getName")
    public String getName(){
        return name;
    }
}
