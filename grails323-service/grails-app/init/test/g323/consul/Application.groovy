package test.g323.consul

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.feign.EnableFeignClients

@EnableFeignClients
@EnableDiscoveryClient  // enable consul discovery
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Override
    Closure doWithSpring() {
        { ->
            // ** spring boot actuator health check indicators ** //

            // override stock consul indicator to avoid null-pointer error during /health endpoint check
            consulHealthIndicator(AppConsulHealthIndicator, ref('consulClient'))
        }
    }
}