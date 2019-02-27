package com.playtika.services.grpc.humidity;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playtika.services.grpc.humidity.common.HumidityDto;

@RestController
public class HumidityController {

    private HumidityProvider humidityProvider;

    @Autowired
    public HumidityController(HumidityProvider humidityProvider) {
        this.humidityProvider = humidityProvider;
    }

    @GetMapping("/api/humidity")
    public HumidityDto getCurrent() {
        return HumidityDto.builder()
                .percent(humidityProvider.getCurrentPercent())
                .timestamp(Instant.now().toEpochMilli())
                .build();
    }

}
