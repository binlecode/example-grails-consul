package test.grails323.service


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

class HelloController {

    @Autowired
    GreetClientFeign greetClientFeign

    @Value('${my.prop:nice-consul}')  // default to 'nice-consul' if not configured in consul config server
    String myProp

    def index(String name) {
        render "hello to: ${name ?: 'consul default name: ' + myProp}"
    }

    def greet(String name) {
        render "get remote greeting: ${greetClientFeign.getGreeting(name)}"
    }
}
