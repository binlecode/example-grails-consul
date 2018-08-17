package test.grails323.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * need to add @EnableFeignClient to boot application class
 * also need to register singleton bean in conf/spring/resources.groovy
 */
@FeignClient(name = "grails335-service", fallback = GreetClientFailover.class)
public interface GreetClientFeign {

    @GetMapping("/greet")
    String getGreeting(@RequestParam("name") String name);

}
