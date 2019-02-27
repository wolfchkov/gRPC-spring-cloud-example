package com.playtika.services.grpc.humidity.client;

import io.grpc.ManagedChannel;

import com.playtika.services.grpc.humidity.common.Empty;
import com.playtika.services.grpc.humidity.common.HumidityGrpc;
import com.playtika.services.grpc.humidity.common.HumidityResponse;

public class HumidityGrpcClient {

    private static final Empty EMPTY = Empty.newBuilder().build();

    private final HumidityGrpc.HumidityBlockingStub humidityStub;

    public HumidityGrpcClient(ManagedChannel managedChannel) {
        humidityStub = HumidityGrpc.newBlockingStub(managedChannel);
    }

    public HumidityResponse getCurrent() {
        return humidityStub.getCurrent(EMPTY);
    }
}
