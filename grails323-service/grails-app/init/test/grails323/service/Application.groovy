package test.grails323.service

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.feign.EnableFeignClients

/**
 *
 * In FeignClient integration, basePackage specification is a must due to Grails classpath overlay.
 * Otherwise the defined java FeignClient interface may not be scanned by SpringBoot under Grails.
 */
@EnableFeignClients(basePackages = ['test.grails323.service'])
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