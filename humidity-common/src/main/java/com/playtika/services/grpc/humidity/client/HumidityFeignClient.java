package com.playtika.services.grpc.humidity.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.playtika.services.grpc.humidity.common.HumidityDto;

@FeignClient("HUMIDITY-SERVICE")
public interface HumidityFeignClient {

    @GetMapping("/api/humidity")
    HumidityDto getCurrent();

}
