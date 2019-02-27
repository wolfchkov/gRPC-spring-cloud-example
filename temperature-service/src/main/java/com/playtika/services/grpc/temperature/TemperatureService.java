package com.playtika.services.grpc.temperature;

import java.time.Instant;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.netty.util.internal.ThreadLocalRandom;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import com.playtika.services.grpc.temperature.common.Empty;
import com.playtika.services.grpc.temperature.common.TemperatureGrpc;
import com.playtika.services.grpc.temperature.common.TemperatureResponse;

@GRpcService
public class TemperatureService extends TemperatureGrpc.TemperatureImplBase {

    private final TemperatureProvider temperatureProvider;

    @Autowired
    public TemperatureService(TemperatureProvider temperatureProvider) {
        this.temperatureProvider = temperatureProvider;
    }

    @Override
    public void getCurrent(Empty request, StreamObserver<TemperatureResponse> responseObserver) {
        //Context.current().getDeadline()
        if (Context.current().isCancelled()) {
            responseObserver.onError(Status.CANCELLED.withDescription("Cancelled by client").asRuntimeException());
            return;
        }

        int temp = temperatureProvider.getCurrentTemp();

        TemperatureResponse temperatureResponse = TemperatureResponse.newBuilder()
                .setTemp(temp)
                .setTimestamp(Instant.now().toEpochMilli())
                .build();

        responseObserver.onNext(temperatureResponse);
        responseObserver.onCompleted();
    }
}
