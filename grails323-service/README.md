# example-grails323-consul

Simple demo Grails application to show how to integrate with Consul for service discovery and health check.
 
## INTRODUCTION 

At this point (July 2018), there's no dedicated Grails plugin for Consul integration. Which is not a big issue as Grails is based on Spring (boot) and Spring boot integration with Consul is trivially easy.
This Grails example is to explain some 3.2 and below specific configuration issues encountered. 

This example is based on Grails 3.2, the compatible spring cloud consul version is 1.2.1.RELEASE. Higher GA versions won't work with Grails 3.2 and below.

In this example application, a custom ```ConsulHealthIndicator``` is defined to override spring stock version to avoid null-pointer exception during consul health check.

A similar issue also seen by [this thread](https://github.com/spring-cloud/spring-cloud-consul/issues/234), where a custom ```ConsulHealthIndicator``` overrides the stock one to disable default health check logic. 

To work with Consul distributed configuration, the example has a dummy controller with a ```@Value``` annotation to fetch shared configuration property from Consul key-value store.  

This example also assumes a running consul server agent to register with. A locally running docker stock Consul container should be sufficient. 


## INSTALL

Check out this code and run the gradle command in terminal:

```bash
./gradlew bootRun
```

## PREREQUISITES

Make sure docker is installed. To start a consul container:
```bash
docker run -d -p 8500:8500 --name dev-consul consul
```
The above command starts a docker consul server agent listening at port 8500 with container name 'dev-consul'.
The running container can be verified by running:
```bash
docker ps
```
or even for runtime resource details:
```bash
docker stats dev-consul
```

## CONFIGURATION


In Grails application check ```grails-app/conf/application.yml``` file for consul specific settings.

```yaml

```

## CHANGELOG

#### v0.1
* simple settings to work with localhost consul agent
* custom ConsulHealthIndicator
* basic consul distributed configuration property support


## CONTRIBUTORS

Bin Le (bin.le.code@gmail.com)


## LICENSE

Apache License Version 2.0. (http://www.apache.org/licenses/)


