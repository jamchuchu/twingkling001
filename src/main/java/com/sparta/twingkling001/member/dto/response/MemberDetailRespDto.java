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
}


