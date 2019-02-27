package com.playtika.services.grpc.temperature;

import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.playtika.services.grpc.temperature.common.Empty;
import com.playtika.services.grpc.temperature.common.TemperatureGrpc;
import com.playtika.services.grpc.temperature.common.TemperatureResponse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TemperatureServiceTest {
    private static final int TEMP = 24;

    @Mock
    private TemperatureProvider temperatureProvider;

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Test
    public void greeterImpl_replyMessage() throws Exception {

        String serverName = InProcessServerBuilder.generateName();

        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(new TemperatureService(temperatureProvider))
                .build().start());


        TemperatureGrpc.TemperatureBlockingStub blockingStub = TemperatureGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName)
                        .directExecutor()
                        .build())
        );

        when(temperatureProvider.getCurrentTemp())
                .thenReturn(TEMP);

        TemperatureResponse temperatureResponse = blockingStub.getCurrent(Empty.newBuilder().build());

        assertEquals(TEMP, temperatureResponse.getTemp());
    }
}