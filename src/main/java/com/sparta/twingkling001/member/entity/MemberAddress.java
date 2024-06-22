package com.sparta.twingkling001.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_address")
public class MemberAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_address_id")
    private Long id;

    @Column(name = "address_detail_id")
    private Long addressDetailId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Column(name = "used_at")
    private LocalDateTime usedAt;
}
