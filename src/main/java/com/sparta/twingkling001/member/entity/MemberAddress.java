package com.sparta.twingkling001.member.entity;

import com.sparta.twingkling001.member.dto.request.MemberAddressReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_address")
public class MemberAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberAddressId;
    private Long addressId;
    private Long memberId;
    private Boolean isPrimary;
    private LocalDateTime usedAt;

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public static MemberAddress from(MemberAddressReqDto reqDto){
        return MemberAddress.builder()
                .memberAddressId(reqDto.getMemberAddressId())
                .addressId(reqDto.getAddressId())
                .memberId(reqDto.getMemberId())
                .isPrimary(reqDto.isPrimary())
                .usedAt(reqDto.getUsedAt())
                .build();
    }
}
