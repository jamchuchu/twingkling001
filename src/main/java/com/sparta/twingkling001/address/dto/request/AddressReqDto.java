package com.sparta.twingkling001.address.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressReqDto {
    private Long addressId;
    private Long zipNumber;
    private String addressMain;
    private String addressDetail;
    private Boolean deletedYn;
}
