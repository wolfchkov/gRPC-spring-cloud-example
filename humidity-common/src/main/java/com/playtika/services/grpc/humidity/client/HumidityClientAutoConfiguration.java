package com.playtika.services.grpc.humidity.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.playtika.services.grpc.discovery.SpringCloudNameResolverProvider;

@Configuration
@ConditionalOnProperty(
        name = "humidity.client.enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableFeignClients(clients = HumidityFeignClient.class)
@PropertySource("classpath:humidity.properties")
@Slf4j
public class HumidityClientAutoConfiguration {
    private static final String LOAD_BALANCING_POLICY = "round_robin";

    @Bean
    public HumidityGrpcClient humidityGrpcClient(@Value("${humidity.grpc.discovery.uri}") String serviceUri,
                                                 DiscoveryClient discoveryClient) {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forTarget(serviceUri)
                .nameResolverFactory(new SpringCloudNameResolverProvider(discoveryClient))
                .defaultLoadBalancingPolicy(LOAD_BALANCING_POLICY)
                .usePlaintext()
                .build();

        return new HumidityGrpcClient(managedChannel);
    }

}
