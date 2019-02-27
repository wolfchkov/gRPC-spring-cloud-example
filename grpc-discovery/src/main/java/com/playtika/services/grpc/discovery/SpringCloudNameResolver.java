package com.playtika.services.grpc.discovery;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@Slf4j
public class SpringCloudNameResolver extends NameResolver {
    private static final String GRPC_PORT_KEY = "grpcPort";
    private static final long DEFAULT_RESOLVE_PERIOD = 5000;
    private static final int SCHEDULE_POOL_SIZE = 1;

    private final String serviceName;
    private final DiscoveryClient discoveryClient;
    private final ScheduledExecutorService updateExecutor;
    private final long resolvePeriod;

    public SpringCloudNameResolver(URI targetUri, DiscoveryClient discoveryClient) {
        this(targetUri, discoveryClient, DEFAULT_RESOLVE_PERIOD);
    }

    public SpringCloudNameResolver(URI targetUri, DiscoveryClient discoveryClient, long resolvePeriod) {
        this.serviceName = targetUri.getAuthority();
        this.discoveryClient = discoveryClient;
        this.resolvePeriod = resolvePeriod;
        this.updateExecutor = Executors.newScheduledThreadPool(SCHEDULE_POOL_SIZE);

    }

    @Override
    public String getServiceAuthority() {
        return serviceName;
    }

    @Override
    public void start(Listener listener) {
        updateExecutor.scheduleWithFixedDelay(new AddressGroupUpdater(listener),0,
                resolvePeriod, TimeUnit.MILLISECONDS);
    }

    @Override
    public void shutdown() {
        updateExecutor.shutdown();

    }

    @AllArgsConstructor
    private class AddressGroupUpdater implements Runnable {

        private final Listener listener;

        @Override
        public void run() {
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);

            List<EquivalentAddressGroup> addressGroups = instances.stream()
                    .filter(instance -> instance.getMetadata().containsKey(GRPC_PORT_KEY))
                    .map(this::toResolvedServerInfo)
                    .collect(Collectors.toList());

            listener.onAddresses(addressGroups,
                    Attributes.EMPTY);
        }

        private EquivalentAddressGroup toResolvedServerInfo(ServiceInstance instance) {
            String host = instance.getHost();
            int port = Integer.parseInt(instance.getMetadata()
                    .get(GRPC_PORT_KEY));

            InetSocketAddress socketAddress = new InetSocketAddress(host, port);
            return new EquivalentAddressGroup(socketAddress);
        }
    }
}
