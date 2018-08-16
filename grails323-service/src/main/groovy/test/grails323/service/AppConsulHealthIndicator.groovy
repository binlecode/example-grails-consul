package test.grails323.service

import com.ecwid.consul.v1.ConsulClient
import com.ecwid.consul.v1.QueryParams
import com.ecwid.consul.v1.Response
import com.ecwid.consul.v1.agent.model.Self
import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health

/**
 * Custom Consul health indicator to avoid null-pointer error during consul health check
 * This should be instantiated during application bootstrap to replace stock {@link org.springframework.cloud.consul.ConsulHealthIndicator}
 * @see {@link Application}
 */
class AppConsulHealthIndicator extends AbstractHealthIndicator {

    private ConsulClient consul

    AppConsulHealthIndicator(ConsulClient consul) {
        this.consul = consul
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try {
            Response<Self> self = consul.getAgentSelf()
            Self.Config config = self.getValue().getConfig()
            Response<Map<String, List<String>>> services = consul
                    .getCatalogServices(QueryParams.DEFAULT)
            builder.up()
                    .withDetail("services", services.getValue() ?: 'unknown')
                    .withDetail("advertiseAddress", config.getAdvertiseAddress() ?: 'unknown')
                    .withDetail("datacenter", config.getDatacenter() ?: 'unknown')
                    .withDetail("domain", config.getDomain() ?: 'unknown')
                    .withDetail("nodeName", config.getNodeName() ?: 'unknown')
                    .withDetail("bindAddress", config.getBindAddress() ?: 'unknown')
                    .withDetail("clientAddress", config.getClientAddress() ?: 'unknown')
        } catch (Exception e) {
            builder.down(e)
        }
    }
}
