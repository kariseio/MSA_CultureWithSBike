package com.sth.congestionservice.model.entity;

import com.sth.congestionservice.model.dto.CongestionDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Congestion {
    @Id
    private String areaNm;
    @Column
    private int areaCongestLvl;
    @Column
    private String areaCongestMsg;
    @Column
    private int fcstCongestLvl;

    public CongestionDTO toDto() {
        return CongestionDTO.builder()
                .areaNm(areaNm)
                .areaCongestLvl(areaCongestLvl)
                .areaCongestMsg(areaCongestMsg)
                .fcstCongestLvl(fcstCongestLvl)
                .build();
    }
}
