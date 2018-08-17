package test.grails323.service;

public class GreetClientFailover implements GreetClientFeign {
    @Override
    public String getGreeting(String name) {
        return "Greeting from fail-over with name: " + name;
    }
}
