package com.sparta.twingkling001.member.dto.response;

import com.sparta.twingkling001.member.entity.MemberDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class MemberDetailRespDto {
    private Long memberDetailId;
    private Long memberId;
    private String age;
    private LocalDate birth;
    private String gender;
    private String name;
    private String nickname;
    private String phoneNumber;

    public MemberDetailRespDto(MemberDetail memberDetail){
        this.memberDetailId = memberDetail.getMemberDetailId();
        this.memberId = memberDetail.getMemberId();
        this.age = memberDetail.getAge();
        this.birth = memberDetail.getBirth();
        this.gender = memberDetail.getGender();
        this.name = memberDetail.getName();
        this.nickname = memberDetail.getNickname();
        this.phoneNumber = memberDetail.getPhoneNumber();
    }
}


