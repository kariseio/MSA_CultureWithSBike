// Sbike.java

package com.sth.sbikeservice.model.entity;

import com.sth.sbikeservice.model.dto.SbikeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sbike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255)
    private String stationId;

    @Column(length = 255)
    private String rackTotCnt;

    @Column(length = 255)
    private String stationName;

    @Column(length = 255)
    private String parkingBikeTotCnt;

    @Column(length = 255)
    private String shared;

    @Column
    private double stationLatitude;

    @Column
    private double stationLongitude;


    public SbikeDTO toDto() {
        return SbikeDTO.builder()
                .stationId(stationId)
                .rackTotCnt(rackTotCnt)
                .stationName(stationName)
                .parkingBikeTotCnt(parkingBikeTotCnt)
                .shared(shared)
                .stationLatitude(String.valueOf(stationLatitude))
                .stationLongitude(String.valueOf(stationLongitude))
                .build();
    }
}
