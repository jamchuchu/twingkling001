package com.sparta.twingkling001.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
public class MemberDetailReqDto {
    private Long memberDetailId;
    private Long memberId;
    private String age;
    private LocalDate birth;
    private String gender;
    private String name;
    private String nickname;
    private String phoneNumber;

}
