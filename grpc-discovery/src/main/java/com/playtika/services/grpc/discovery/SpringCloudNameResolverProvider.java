package com.playtika.services.grpc.discovery;

import java.net.URI;

import javax.annotation.Nullable;

import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@Slf4j
public class SpringCloudNameResolverProvider extends NameResolverProvider {

    protected static final String DISCOVERY = "discovery";

    private final DiscoveryClient discoveryClient;

    public SpringCloudNameResolverProvider(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 6;
    }

    @Nullable
    @Override
    public NameResolver newNameResolver(URI uri, Attributes attributes) {
        return new SpringCloudNameResolver(uri, discoveryClient);
    }

    @Override
    public String getDefaultScheme() {
        return DISCOVERY;
    }

}
