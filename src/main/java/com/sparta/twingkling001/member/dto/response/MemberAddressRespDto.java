package com.sparta.twingkling001.member.dto.response;

import com.sparta.twingkling001.member.entity.MemberAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberAddressRespDto {
    private Long memberAddressId;
    private Long memberId;
    private Long AddressId;
    private boolean isPrimary;
    private LocalDateTime UsedAt;

    public static MemberAddressRespDto from(MemberAddress memberAddress) {
        return MemberAddressRespDto.builder()
                .memberAddressId(memberAddress.getMemberAddressId())
                .memberId(memberAddress.getMemberId())
                .AddressId(memberAddress.getAddressId())
                .isPrimary(memberAddress.getIsPrimary())
                .UsedAt(memberAddress.getUsedAt())
                .build();

    }
}
