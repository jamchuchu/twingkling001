package com.sparta.twingkling001.address.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long AddressId;

    @Column(name = "address_main")
    private String addressMain;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "zip_number")
    private Long zipNumber;

    @Column(name = "deleted_yn")
    private Boolean deletedYn;
}
