import test.grails323.service.GreetClientFailover

// Place your Spring DSL code here
beans = {

    // register singleton feign client hysrtrix bean for failover
    greetClientFailover(GreetClientFailover)

}
