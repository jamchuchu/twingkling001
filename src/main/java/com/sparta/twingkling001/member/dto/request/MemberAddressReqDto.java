package com.sparta.twingkling001.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class MemberAddressReqDto {
    private Long memberAddressId;
    private Long memberId;
    private Long addressId;
    @JsonProperty("isPrimary")
    private boolean isPrimary;
    private LocalDateTime usedAt;
}
