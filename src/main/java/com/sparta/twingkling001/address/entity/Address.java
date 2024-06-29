package com.sparta.twingkling001.address.entity;

import com.sparta.twingkling001.address.dto.request.AddressReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;
    private Long zipNumber;
    private String addressMain;
    private String addressDetail;
    private Boolean deletedYn;

    public void setZipNumber(Long zipNumber) {
        this.zipNumber = zipNumber;
    }

    public void setAddressMain(String addressMain) {
        this.addressMain = addressMain;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public void setDeletedYn(Boolean deletedYn) {
        this.deletedYn = deletedYn;
    }

    public static Address from(AddressReqDto reqDto){
        return Address.builder()
                .addressId(reqDto.getAddressId())
                .zipNumber(reqDto.getZipNumber())
                .addressMain(reqDto.getAddressMain())
                .addressDetail(reqDto.getAddressDetail())
                .deletedYn(reqDto.getDeletedYn())
                .build();
    }
}
