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

In Grails application check ```grails-app/conf/application.yml``` file for consul specific settings.
For example, for grails323-service:

```yaml
spring:
    # spring cloud consul integration settings
    # By default, taken from the Environment:
    #  - service name is ${spring.application.name}
    #  - instance id is the Spring Context ID, which by default is
    #    ${spring.application.name}:comma,separated,profiles:${server.port}
    #  - port is ${server.port}
    application:
        name: grails323-service
    cloud:
        consul:
#            host: localhost
#            port: 8500
#            discovery:
#                health-check-path: /health
#                healthCheckInterval: 15s
            ## To enable consul distributed configuration:
            config:
                enabled: true
```

## PREREQUISITES
For this example, a locally running Consul should be running on default port 8500

#### Option 1 - install consul binary to hosting OS

This depends on your OS, but the [process is simple](https://www.consul.io/intro/getting-started/install.html).

For MacOS, the recommended way is install via [homebrew](https://brew.sh/).

```bash
# check consul bottle info in homebrew
brew info consul
# install
brew install consul
```

To run consul locally, simply do:
```bash
consul agent -dev
```
This consul server is running in dev mode, which is useful for bringing up a single-node Consul environment quickly and easily with default settings such as port 8500.
Check [this page](https://www.consul.io/intro/getting-started/agent.html) for basic consul shell commands.

#### Option 2 - run consul as docker container 

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


## INSTALL

Check out this code repo.

#### Simple node service discovery

The simple way of running both service nodes with consul
```bash
./gradlew :grails335-service:bootRun
./gradlew :grails323-service:bootRun
```
As defined in ```application.yml```, the services are running on port 8090 and 8091 respectively. Change to other values as needed.

#### Service node cluster and client-side load balancing

Based on SpringBoot profile support, Grails support per environment configuration with JVM option ```-Dgrails.env```.
The grails335-service supports two additional custom environment ```p8190``` and ```p8290``` for custom ports (8190 and 8290), so that a cluster of two nodes can be registered to consul under same service ```grails335-service```.  

```bash
# run these two gradle command in two separate terminals
./gradlew :grails355-service:bootRun -Dgrails.env=p8190
./gradlew :grails355-service:bootRun -Dgrails.env=p8290
```

The grails323-service now works as a service client to call endpoint ```grails335-service/hello/greet?name=<some-name>```.
The service client is based on ```FeignClient``` with client-side load balancing built-in. 

By calling ```grails323-service/greet?name=<some-name>``` multiple times you will find the response shows port number keeps changing between 8190 and 8290 from cloud service.
And, if one grails335-service node is shut down, all responses will only come from the other port. 

#### Service fail-over based on circuit breaker with Hystrix

Feign client has circuit breaker functionality built-in basd on Hystrix. Only one property in ```application.yml``` is needed to enable it.
```yaml
feign:
    hystrix:
        enabled: true
```

With hystrix enabled, the ```@FeignClient``` annotation can support ```fallback``` method to use a fail-over class that implements the annotated interface. 
In grails323-service application, such a class is defined as a backup for greet client call when grails3350-service endpoint is down.

Shut down all grails335-service nodes, and try calling ```grails323-service/hello/greet``` again, you will receive failover response instead. 

Note in Grails, a spring bean is registered in ```grails-app/conf/resources.groovy``` file, not by ```@Component``` annotation as in pure Spring framework. 

## CONSUL HOUSEKEEPING

#### get all registered services
```bash
curl --request GET http://localhost:8500/v1/agent/services
```

#### de-register a service
```bash
curl --request PUT \
http://localhost:8500/v1/agent/service/deregister/<obsolete-service-id>
```

## DISTRIBUTED CONFIGURATION

To load a key-value pair to consul KV store:
```bash
consul kv put config/grails323-service/hello/name "Consul-KV-Name"
```
the key above corresponds grails323-service application ```HelloController```'s spring ```@Value``` annotation with value ```hello.name```.

This key value pair can be removed by:
```bash
consul kv delete config/grails323-service/hello/name
```

## CHANGELOG

#### v0.4
* add basic consul distributed configuration property support

#### v0.3
* add Feign client Hystrix failover example

#### v0.2
* add Feign client example for service discovery and remote call
* rewrite README to make it 'readable'

#### v0.1
* simple settings to work with localhost consul agent
* custom ConsulHealthIndicator


## CONTRIBUTORS

Bin Le (bin.le.code@gmail.com)


## LICENSE

Apache License Version 2.0. (http://www.apache.org/licenses/)


