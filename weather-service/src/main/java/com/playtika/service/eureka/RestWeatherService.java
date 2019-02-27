package com.playtika.service.eureka;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import com.playtika.services.grpc.humidity.client.HumidityFeignClient;
import com.playtika.services.grpc.temperature.client.TemperatureFeignClient;

@AllArgsConstructor
@Component
public class RestWeatherService {

    private final TemperatureFeignClient temperatureClient;
    private final HumidityFeignClient humidityClient;

    public WeatherDTO getWeather() {
        return WeatherDTO.builder()
                .temp(temperatureClient.getCurrent().getTemp())
                .humidity(humidityClient.getCurrent().getPercent())
                .build();
    }


}
