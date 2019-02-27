package com.playtika.services.grpc.temperature.client;

import io.grpc.ManagedChannel;

import com.playtika.services.grpc.temperature.common.Empty;
import com.playtika.services.grpc.temperature.common.TemperatureGrpc;
import com.playtika.services.grpc.temperature.common.TemperatureResponse;

public class TemperatureGrpcClient {

    private static final Empty EMPTY = Empty.newBuilder().build();

    private final TemperatureGrpc.TemperatureBlockingStub temperatureStub;

    public TemperatureGrpcClient(ManagedChannel managedChannel) {
        temperatureStub = TemperatureGrpc.newBlockingStub(managedChannel);
    }

    public TemperatureResponse getCurrent() {
        return temperatureStub.getCurrent(EMPTY);
    }
}
