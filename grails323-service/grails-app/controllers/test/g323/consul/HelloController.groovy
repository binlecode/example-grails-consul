package test.g323.consul

import org.springframework.beans.factory.annotation.Value

class HelloController {

    @Value('${my.prop:nice-consul}')  // default to 'nice-consul' if not configured in consul config server
    String myProp

    def index(String name) {
        render "greet to: ${name ?: 'consul default name: ' + myProp}"
    }
}
