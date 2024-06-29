package com.sparta.twingkling001.member.entity;

import com.sparta.twingkling001.member.dto.request.MemberDetailReqDto;
import com.sparta.twingkling001.member.repository.MemberRepository;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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
    private long memberDetailId;
    private long memberId;
    private String name;
    private String nickname;
    private String phoneNumber;
    private String age;
    private String gender;
    private LocalDate birth;

    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public static MemberDetail from(MemberDetailReqDto reqDto){
        return MemberDetail.builder()
                .memberDetailId(reqDto.getMemberDetailId())
                .memberId(reqDto.getMemberId())
                .name(reqDto.getName())
                .nickname(reqDto.getNickname())
                .phoneNumber(reqDto.getPhoneNumber())
                .age(reqDto.getAge())
                .gender(reqDto.getGender())
                .birth(reqDto.getBirth())
                .build();
    }

}

