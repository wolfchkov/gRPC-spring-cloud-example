package com.playtika.services.grpc.temperature;

import io.netty.util.internal.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class TemperatureProvider {
    private static final int MIN_TEMPERATURE = -50;
    private static final int MAX_TEMPERATURE  = 51;

    public int getCurrentTemp() {
        return ThreadLocalRandom.current().nextInt(MIN_TEMPERATURE, MAX_TEMPERATURE);
    }
}
