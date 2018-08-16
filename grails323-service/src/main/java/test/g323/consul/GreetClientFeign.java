package test.g323.consul;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * need to add @EnableFeignClient to boot application class
 */
@FeignClient
public interface GreetClientFeign {

    @GetMapping("/greet/{name}")
    String getGreeting(@PathVariable String name);

}
