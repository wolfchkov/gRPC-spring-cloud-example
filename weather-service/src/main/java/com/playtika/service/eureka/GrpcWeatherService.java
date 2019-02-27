package com.playtika.service.eureka;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.playtika.services.grpc.humidity.client.HumidityGrpcClient;
import com.playtika.services.grpc.temperature.client.TemperatureGrpcClient;

@Service
@AllArgsConstructor
public class GrpcWeatherService {

    private final TemperatureGrpcClient temperatureClient;
    private final HumidityGrpcClient humidityClient;

    public WeatherDTO getWeather() {
        return WeatherDTO.builder()
                .temp(temperatureClient.getCurrent().getTemp())
                .humidity(humidityClient.getCurrent().getPercent())
                .build();
    }


}
