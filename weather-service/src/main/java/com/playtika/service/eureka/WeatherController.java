package com.playtika.service.eureka;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
class WeatherController {

    private final GrpcWeatherService grpcWeatherService;
    private final RestWeatherService restWeatherService;

    @GetMapping("/api/grpc/weather")
    WeatherDTO getWeatherByGrpc() {
        return grpcWeatherService.getWeather();
    }

    @GetMapping("/api/rest/weather")
    WeatherDTO getWeatherByRest() {
        return restWeatherService.getWeather();
    }
}
