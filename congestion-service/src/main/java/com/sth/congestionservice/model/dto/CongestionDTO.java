package com.sth.congestionservice.model.dto;

import com.sth.congestionservice.model.entity.Congestion;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CongestionDTO {
    private String areaNm;
    private int areaCongestLvl;
    private String areaCongestMsg;
    private int fcstCongestLvl;

    public Congestion toEntity() {
        return Congestion.builder()
                .areaNm(areaNm)
                .areaCongestLvl(areaCongestLvl)
                .areaCongestMsg(areaCongestMsg)
                .fcstCongestLvl(fcstCongestLvl)
                .build();
    }
}
