package com.sparta.twingkling001.member.dto.response;

import com.sparta.twingkling001.member.entity.MemberAddress;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class MemberAddressRespDto {
    private Long memberAddressId;
    private Long memberId;
    private Long AddressId;
    private boolean isPrimary;
    private LocalDateTime UsedAt;

    public MemberAddressRespDto(MemberAddress memberAddress) {
        this.memberAddressId = memberAddress.getMemberAddressId();
        this.memberId = memberAddress.getMemberId();
        this.AddressId = memberAddress.getAddressId();
        this.isPrimary = memberAddress.getIsPrimary();
        this.UsedAt = memberAddress.getUsedAt();
    }
}
