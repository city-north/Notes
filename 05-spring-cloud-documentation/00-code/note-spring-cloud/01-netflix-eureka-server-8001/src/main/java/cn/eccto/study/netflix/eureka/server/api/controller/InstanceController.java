package cn.eccto.study.netflix.eureka.server.api.controller;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description
 *
 * @author EricChen 2019/11/29 16:52
 */
@RestController
public class InstanceController {

    private DiscoveryClient discoveryClient;

    public InstanceController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping({"/discovery-clients"})
    public List<DiscoveryClient> serviceInstancesByApplicationName() {
        return ((CompositeDiscoveryClient) discoveryClient).getDiscoveryClients();
    }
}
