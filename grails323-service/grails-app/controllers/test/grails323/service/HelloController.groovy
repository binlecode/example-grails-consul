package test.grails323.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

class HelloController {

    @Autowired
    GreetClientFeign greetClientFeign

    /**
     * look up consul config store for key path: config/grails323-service/hello/name
     */
    @Value('${hello.name:nice-consul}')  // default to 'nice-consul' if not configured in consul config server
    String name

    def index(String name) {
        render "hello to: ${name ?: 'consul default name: ' + this.name}"
    }

    def greet(String name) {
        render "get remote greeting: ${greetClientFeign.getGreeting(name)}"
    }
}
