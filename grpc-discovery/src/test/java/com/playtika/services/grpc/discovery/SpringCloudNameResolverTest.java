package com.playtika.services.grpc.discovery;

import java.net.URI;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpringCloudNameResolverTest {

    private static final String SERVICE_NAME = "humidity-service";

    private SpringCloudNameResolver springCloudNameResolver;

    private URI serviceUri = URI.create("discovery://humidity-service") ;

    @Mock
    private DiscoveryClient discoveryClient;

    @Before
    public void setUp() {
        springCloudNameResolver = new SpringCloudNameResolver(serviceUri, discoveryClient);
    }

    @Test
    public void shouldReturnServiceAuthority() {
        assertEquals(SERVICE_NAME, springCloudNameResolver.getServiceAuthority());
    }

    @Test
    public void start() throws InterruptedException {
        ServiceInstance instance1 = mock(ServiceInstance.class);
        when(instance1.getHost())
                .thenReturn("192.168.1.100");
        when(instance1.getMetadata())
                .thenReturn(ImmutableMap.of("grpcPort", "11555"));

        ServiceInstance instance2 = mock(ServiceInstance.class);
        when(instance2.getHost())
                .thenReturn("192.168.1.200");
        when(instance2.getMetadata())
                .thenReturn(ImmutableMap.of("grpcPort", "11666"));

        when(discoveryClient.getInstances(SERVICE_NAME))
                .thenReturn(Lists.newArrayList(instance1, instance2));


        NameResolver.Listener listener = mock(NameResolver.Listener.class);
        springCloudNameResolver.start(listener);

        Thread.sleep(500);

        ArgumentCaptor<List<EquivalentAddressGroup>> captor = ArgumentCaptor.forClass(List.class);
        verify(listener).onAddresses(captor.capture(), eq(Attributes.EMPTY));

        List<EquivalentAddressGroup> equivalentAddressGroups = captor.getValue();

        assertEquals(2, equivalentAddressGroups.size());
    }
}