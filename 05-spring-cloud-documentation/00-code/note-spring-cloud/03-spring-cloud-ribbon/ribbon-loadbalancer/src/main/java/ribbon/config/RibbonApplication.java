package ribbon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/10/10 19:54
 */
@SpringBootApplication
@RestController
@RibbonClient(name = "say-hello", configuration = RibbonConfiguration.class)
public class RibbonApplication {

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class, args);
    }

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/hi")
    public String hi(@RequestParam(value = "name", defaultValue = "Artaban") String name) {
        String greeting = this.restTemplate.getForObject("http://say-hello/greeting", String.class);
        return String.format("%s, %s!", greeting, name);
    }
}
