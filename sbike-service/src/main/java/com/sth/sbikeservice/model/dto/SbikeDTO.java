package com.sth.sbikeservice.model.dto;

import com.sth.sbikeservice.model.entity.Sbike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SbikeDTO {
    private String CODENAME;
    private String GUNAME;
    private String TITLE;
    private String STRTDATE;
    private String END_DATE;
    private String PLACE;
    private String USE_FEE;
    private String PLAYER;
    private String PROGRAM;
    private String ORG_LINK;
    private double LOT;
    private double LAT;

    public Sbike toEntity() {
        return Sbike.builder()
                .CODENAME(CODENAME)
                .GUNAME(GUNAME)
                .TITLE(TITLE)
                .STRTDATE(STRTDATE)
                .END_DATE(END_DATE)
                .PLACE(PLACE)
                .USE_FEE(USE_FEE)
                .PLAYER(PLAYER)
                .PROGRAM(PROGRAM)
                .ORG_LINK(ORG_LINK)
                .LOT(LOT)
                .LAT(LAT)
                .build();
    }
}