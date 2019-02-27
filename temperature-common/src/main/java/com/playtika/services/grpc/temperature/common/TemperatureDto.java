package com.playtika.services.grpc.temperature.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemperatureDto {

    private int temp;
    private long timestamp;
}
