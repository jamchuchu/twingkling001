package com.sparta.twingkling001.member.entity;

import com.sparta.twingkling001.member.dto.request.MemberDetailReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_detail")

public class MemberDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_detail_id")
    private Long memberDetailId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "age")
    private String age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth")
    private LocalDate birth;

     public MemberDetail(MemberDetailReqDto reqDto){
         this.memberId = reqDto.getMemberId();
         this.age = reqDto.getAge();
         this.birth = reqDto.getBirth();
         this.gender = reqDto.getGender();
         this.name = reqDto.getName();
         this.nickname = reqDto.getNickname();
         this.phoneNumber = reqDto.getPhoneNumber();
     }
}

