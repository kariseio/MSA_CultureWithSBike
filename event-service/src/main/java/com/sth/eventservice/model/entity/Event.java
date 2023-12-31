package com.sth.eventservice.model.entity;

import com.sth.eventservice.model.dto.EventDTO;
import jakarta.persistence.*;

import jakarta.xml.bind.annotation.XmlElement;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(unique = true)
    private String eventNm;

    @Column(nullable = false,length = 20)  //2023.12.13.13:49
    private String areaNm;

    @Column(length = 255)
    private String guname;

    @Column(length = 255)
    private String title;

    @Column(length = 255)
    private String codename;

    private LocalDate strtdate;

    private LocalDate endDate;

    @Column(length = 255)
    private String place;

    @Column(length = 1000)
    private String program;

    @Column(length = 255)
    private String useFee;

    @Column(length = 1000)
    private String orgLink;

    private Double lot;

    private Double lat;

    @Column(length = 1000)
    private String player;

    @Column(length = 1000)
    private String mainImg;


    public EventDTO toDto() {
        return EventDTO.builder()
                .eventId(eventId)
                .eventNm(eventNm)
                .areaNm(areaNm)
                .guname(guname)
                .codename(codename)
                .strtdate(strtdate)
                .endDate(endDate)
                .title(title)
                .place(place)
                .program(program)
                .useFee(useFee)
                .orgLink(orgLink)
                .lot(lot)
                .lat(lat)
                .player(player)
                .mainImg(mainImg)
                .build();
    }
}