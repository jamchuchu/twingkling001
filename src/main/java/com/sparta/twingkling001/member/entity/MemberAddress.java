package com.sparta.twingkling001.member.entity;

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
    @Column(name = "member_address_id")
    private Long memberAddressId;

    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Column(name = "used_at")
    private LocalDateTime usedAt;
}
