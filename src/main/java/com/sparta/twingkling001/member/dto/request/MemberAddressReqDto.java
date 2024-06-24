package com.sparta.twingkling001.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberAddressReqDto {
    private Long memberId;
    private Long AddressId;
    private boolean isPrimary;
}
