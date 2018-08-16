# example-grails-consul

Simple demo Grails applications cluster to show how to integrate with Consul for service registration, health check, cloud config, service discovery and remote service call.
 
## INTRODUCTION 

At this point (July 2018), there's no dedicated Grails plugin for Consul integration. Which is not a big issue as Grails is based on Spring (boot) and Spring boot integration with Consul is trivially easy.
This Grails example is to show how to integrate with Consul with Grails v3.2 and v3.3. Especially some v3.2 and below specific configuration issues encountered as its underlying (relatively older) Spring cloud version compatibility. 

For Grails 3.2, the compatible spring cloud consul version is 1.2.1.RELEASE. Higher GA versions won't work with Grails 3.2 and below. 
* In this version of Spring boot, a custom ```ConsulHealthIndicator``` is defined to override spring stock version to avoid null-pointer exception during consul health check.
* A similar issue also seen by [this thread](https://github.com/spring-cloud/spring-cloud-consul/issues/234), where a custom ```ConsulHealthIndicator``` overrides the stock one to disable default health check logic. 

#### service registration and health check

Both Grails v323 and v335 apps share the same annotation ```@EnableDiscoveryClient``` to support service discovery with Consul. Same as Eureka integration, no tricks.
For health check is based on SpringBoot Actuator, therefore all framework given health indicators are supported in consul health check.  Grails v323 needs a custom  healthIndicator as explained above.

Same as Eureka discovery server handshake design, the consul server won't initiate a health check refresh upon a client registration, but stick to its refresh cycle (default 30s) instead.
Make sure the local OS hosts file has localhost entry for defined service names, ```grails323-service```, and ```grails335-service``` in this case.
```bash
sudo vi /etc/hosts
```
and in hosts file add:
```bash
127.0.0.1 grails323-service
127.0.0.1 grails335-service
```

#### cloud configuration

To work with Consul distributed configuration, the Grails v323 service application has a dummy controller with a ```@Value``` annotation to fetch shared configuration property from Consul key-value store.  

This example assumes the running consul server agent registers the corresponding property in its key/value store. 

#### service discovery

TBD

## PREREQUISITES
For this example, a locally running docker stock Consul container should be sufficient. 
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

## INSTALL

Check out this code repo.

The simple way of running both service nodes with consul
```bash
./gradlew :grails335-service:bootRun
./gradlew :grails323-service:bootRun
```
As defined in ```application.yml```, the services are running on port 8090 and 8091 respectively. Change to other values as needed.

//todo: add example and jar command to run multiple nodes with profile specific ports


## CONSUL HOUSEKEEPING

#### get all registered services
```bash
curl --request GET http://localhost:8500/v1/agent/services
```

```bash
curl --request PUT \
http://localhost:8500/v1/agent/service/deregister/<obsolete-service-id>
```

## CHANGELOG

#### v0.2
* add Feign client example for service discovery and remote call
* rewrite README to make it 'readable'

#### v0.1
* simple settings to work with localhost consul agent
* custom ConsulHealthIndicator
* basic consul distributed configuration property support


## CONTRIBUTORS

Bin Le (bin.le.code@gmail.com)


## LICENSE

Apache License Version 2.0. (http://www.apache.org/licenses/)


