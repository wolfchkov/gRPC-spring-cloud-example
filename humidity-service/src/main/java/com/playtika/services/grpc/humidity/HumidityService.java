package com.playtika.services.grpc.humidity;

import java.time.Instant;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import io.netty.util.internal.ThreadLocalRandom;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import com.playtika.services.grpc.humidity.common.Empty;
import com.playtika.services.grpc.humidity.common.HumidityGrpc;
import com.playtika.services.grpc.humidity.common.HumidityResponse;

@GRpcService
public class HumidityService extends HumidityGrpc.HumidityImplBase {

    private final HumidityProvider humidityProvider;

    @Autowired
    public HumidityService(HumidityProvider humidityProvider) {
        this.humidityProvider = humidityProvider;
    }

    @Override
    public void getCurrent(Empty request, StreamObserver<HumidityResponse> responseObserver) {
        //Context.current().getDeadline()
        if (Context.current().isCancelled()) {
            responseObserver.onError(Status.CANCELLED.withDescription("Cancelled by client").asRuntimeException());
            return;
        }

        HumidityResponse temperatureResponse = HumidityResponse.newBuilder()
                .setPercent(humidityProvider.getCurrentPercent())
                .setTimestamp(Instant.now().toEpochMilli())
                .build();

        responseObserver.onNext(temperatureResponse);
        responseObserver.onCompleted();
    }
}
