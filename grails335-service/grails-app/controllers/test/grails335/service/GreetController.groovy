package test.grails335.service

import org.springframework.beans.factory.annotation.Value

class GreetController {


    @Value('${local.server.port}')
    String port

    def index(String name) {
        render "== Hello ${name ?: 'CONSUL CLIENT'} from Grails335 service with port ${port} =="
    }
}
