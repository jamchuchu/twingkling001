package com.sparta.twingkling001.member.dto.response;

import com.sparta.twingkling001.member.entity.MemberDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
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

    public static MemberDetailRespDto from(MemberDetail memberDetail){
        return  MemberDetailRespDto.builder()
                .memberDetailId(memberDetail.getMemberDetailId())
                .memberId(memberDetail.getMemberId())
                .age(memberDetail.getAge())
                .birth(memberDetail.getBirth())
                .gender(memberDetail.getGender())
                .name(memberDetail.getName())
                .nickname(memberDetail.getNickname())
                .phoneNumber(memberDetail.getPhoneNumber())
                .build();
    }
}


