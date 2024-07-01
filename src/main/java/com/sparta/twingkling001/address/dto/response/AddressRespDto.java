package com.sparta.twingkling001.address.dto.response;

import com.sparta.twingkling001.address.dto.request.AddressReqDto;
import com.sparta.twingkling001.address.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressRespDto {
    private Long addressId;
    private Long zipNumber;
    private String addressMain;
    private String addressDetail;
    private Boolean deletedYn;

    public static AddressRespDto from(Address addr){
        return AddressRespDto.builder()
                .addressId(addr.getAddressId())
                .zipNumber(addr.getZipNumber())
                .addressMain(addr.getAddressMain())
                .addressDetail(addr.getAddressDetail())
                .deletedYn(addr.getDeletedYn())
                .build();
    }
}
