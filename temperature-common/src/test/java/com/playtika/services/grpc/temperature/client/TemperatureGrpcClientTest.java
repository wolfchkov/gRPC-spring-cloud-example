package com.playtika.services.grpc.temperature.client;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import io.grpc.util.MutableHandlerRegistry;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.playtika.services.grpc.temperature.common.Empty;
import com.playtika.services.grpc.temperature.common.TemperatureGrpc;
import com.playtika.services.grpc.temperature.common.TemperatureResponse;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class TemperatureGrpcClientTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private final MutableHandlerRegistry serviceRegistry = new MutableHandlerRegistry();

    private TemperatureGrpcClient temperatureClient;

    @Before
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();

        grpcCleanup.register(
                InProcessServerBuilder.forName(serverName)
                        .directExecutor()
                        .fallbackHandlerRegistry(serviceRegistry)
                        .build()
                        .start());

        ManagedChannel channel = grpcCleanup.register(
                InProcessChannelBuilder.forName(serverName)
                        .directExecutor()
                        .build());

        temperatureClient = new TemperatureGrpcClient(channel);
    }

    @Test
    public void shouldGetMessageFromServer() {
        final TemperatureResponse response = TemperatureResponse.newBuilder()
                .setTemp(10)
                .setTimestamp(10000L)
                .build();

        TemperatureGrpc.TemperatureImplBase stubService = new TemperatureGrpc.TemperatureImplBase() {
            public void getCurrent(Empty request, StreamObserver<TemperatureResponse> responseObserver) {
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
        serviceRegistry.addService(stubService);

        TemperatureResponse temperatureResponse = temperatureClient.getCurrent();

        assertEquals(response, temperatureResponse);
    }
}