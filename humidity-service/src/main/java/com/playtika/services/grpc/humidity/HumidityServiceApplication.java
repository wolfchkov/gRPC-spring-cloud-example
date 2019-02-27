package com.playtika.services.grpc.humidity;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
public class HumidityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HumidityServiceApplication.class, args);
	}

}

