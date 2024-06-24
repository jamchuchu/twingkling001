package com.sparta.twingkling001.member.service;


import com.sparta.twingkling001.member.dto.request.MemberDetailReqDto;
import com.sparta.twingkling001.member.dto.request.MemberReqDtoByMail;
import com.sparta.twingkling001.member.dto.response.MemberDetailRespDto;

import com.sparta.twingkling001.member.entity.Member;
import com.sparta.twingkling001.member.entity.MemberDetail;
import com.sparta.twingkling001.member.repository.MemberDetailRepository;
import com.sparta.twingkling001.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final PasswordEncoder passwordEncoder;

    public long addMember(MemberReqDtoByMail memberReqDtoByMail) {
        Member member = Member.builder()
                .roleId(1L)
                .email(memberReqDtoByMail.getEmail())
                .password(passwordEncoder.encode(memberReqDtoByMail.getPassword()))
                .createdAt(LocalDateTime.now())
                .deletedYn(false)
                .build();
        return memberRepository.save(member).getMemberId();
    }

    //이메일 형식 체크
    public boolean checkEmailForm(String email)   {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }
    //중복 체크
    public boolean checkDuple(String email)   {
        Long size = memberRepository.getMemberSizeByEmail(email);
        if(size != 0){
            return false;
        }
        return true;
    }

    //회원가입시 멤버 디테일 추가
    public Long addMemberDetail(MemberDetailReqDto reqDto) {
        MemberDetail memberDetail = new MemberDetail(reqDto);
        return  memberDetailRepository.save(memberDetail).getMemberId();
    }
    //조회
    public MemberDetailRespDto getMemberDetail(long memberId) {
        MemberDetail memberDetail = memberDetailRepository.getMemberDetailByMemberId(memberId);
        MemberDetailRespDto respDto = MemberDetailRespDto.builder()
                .memberDetailId(memberDetail.getMemberDetailId())
                .memberId(memberDetail.getMemberId())
                .age(memberDetail.getAge())
                .birth(memberDetail.getBirth())
                .gender(memberDetail.getGender())
                .name(memberDetail.getName())
                .nickname(memberDetail.getNickname())
                .phoneNumber(memberDetail.getPhoneNumber())
                .build();
        return respDto;
    }
    //수정
    public void updateMemberDetail(MemberDetailReqDto reqDto) {
        MemberDetail memberDetail = new MemberDetail(reqDto);
        memberDetailRepository.updateMemberDetail(memberDetail);
    }
    //삭제
    public void deleteMemberDetail(Long memberId) {
        memberDetailRepository.deleteMemberDetailByMemberIdEquals(memberId);
    }

}
