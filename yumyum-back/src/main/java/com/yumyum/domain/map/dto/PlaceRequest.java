package com.yumyum.domain.map.dto;

import com.yumyum.domain.map.entity.Place;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceRequest {

    private Long id;
    private String addressName;
    private String phone;
    private String placeName;
    private Double y;
    private Double x;

    public Place toEntity(){
        return Place.builder()
                .id(id)
                .addressName(addressName.trim())
                .phone(phone.trim())
                .placeName(placeName)
                .x(x)
                .y(y)
                .build();
    }
}