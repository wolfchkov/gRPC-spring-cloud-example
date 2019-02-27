package com.playtika.services.grpc.humidity.client;

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

import com.playtika.services.grpc.humidity.common.Empty;
import com.playtika.services.grpc.humidity.common.HumidityGrpc;
import com.playtika.services.grpc.humidity.common.HumidityResponse;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class HumidityGrpcClientTest {
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private final MutableHandlerRegistry serviceRegistry = new MutableHandlerRegistry();

    private HumidityGrpcClient humidityClient;

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

        humidityClient = new HumidityGrpcClient(channel);
    }

    @Test
    public void shouldGetMessageFromServer() {
        final HumidityResponse response = HumidityResponse.newBuilder()
                .setPercent(10)
                .setTimestamp(10000L)
                .build();

        HumidityGrpc.HumidityImplBase stubService = new HumidityGrpc.HumidityImplBase() {
            public void getCurrent(Empty request, StreamObserver<HumidityResponse> responseObserver) {
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
        serviceRegistry.addService(stubService);

        HumidityResponse humidityResponse = humidityClient.getCurrent();

        assertEquals(response, humidityResponse);
    }
}