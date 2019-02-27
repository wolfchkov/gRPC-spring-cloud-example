package com.playtika.services.grpc.humidity.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HumidityDto {

    private int percent;
    private long timestamp;
}
