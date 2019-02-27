package com.playtika.services.grpc.temperature.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.playtika.services.grpc.temperature.common.TemperatureDto;

@FeignClient("TEMPERATURE-SERVICE")
public interface TemperatureFeignClient {

    @GetMapping("/api/temperature")
    TemperatureDto getCurrent();

}
