package gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/06/16 19:47
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping()
    public String test(){
        return "SUCCESS";
    }
}
