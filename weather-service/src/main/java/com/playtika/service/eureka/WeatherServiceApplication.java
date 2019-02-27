package com.playtika.service.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
public class WeatherServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherServiceApplication.class, args);
    }

}
