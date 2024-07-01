package com.sparta.twingkling001.member.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.twingkling001.api.exception.ErrorType;
import com.sparta.twingkling001.member.dto.request.MemberDetailReqDto;
import com.sparta.twingkling001.member.dto.request.MemberReqDtoByMail;
import com.sparta.twingkling001.member.dto.response.MemberDetailRespDto;

import com.sparta.twingkling001.member.dto.response.MemberRespDto;
import com.sparta.twingkling001.member.entity.Member;
import com.sparta.twingkling001.member.entity.MemberDetail;
import com.sparta.twingkling001.member.entity.Role;
import com.sparta.twingkling001.member.repository.MemberDetailRepository;
import com.sparta.twingkling001.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberDetailRepository memberDetailRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public long addMember(MemberReqDtoByMail memberReqDtoByMail) {
        Member member = Member.builder()
                .role(Role.USER)
                .email(memberReqDtoByMail.getEmail())
                .password(passwordEncoder.encode(memberReqDtoByMail.getPassword()))
                .createdAt(LocalDateTime.now())
                .deletedYn(false)
                .build();
        return memberRepository.save(member).getMemberId();
    }

    public MemberRespDto getMemberByMemberId(long memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId).orElse(null);

        if(member == null){
            throw new IllegalArgumentException(ErrorType.NOT_FOUND_MEMBER.getMessage());
        }
        return MemberRespDto.from(member);
    }

    public MemberRespDto getMemberByEmail(String email){
        Member member = memberRepository.findByEmail(email).orElse(null);
        return MemberRespDto.from(member);
    }

    @Transactional
    public void updateMemberPassword(Long memberId, String password){
        Member member = entityManager.find(Member.class, memberId);
        member.setPassword(passwordEncoder.encode(password));
    }

    //이미 삭제 된것 추후 확인 필요
    public void deleteMember(Long memberId){
        Member member = entityManager.find(Member.class, memberId);
        member.setDeletedYn(true);
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
        Long size = memberRepository.countMemberByEmail(email);
        if(size != 0){
            return false;
        }
        return true;
    }

    //회원가입시 멤버 디테일 추가
    public Long addMemberDetail(MemberDetailReqDto reqDto) {
        MemberDetail memberDetail = MemberDetail.from(reqDto);
        return  memberDetailRepository.save(memberDetail).getMemberId();
    }
    //조회
    public MemberDetailRespDto getMemberDetail(long memberId) {
        MemberDetail memberDetail = memberDetailRepository.getMemberDetailByMemberId(memberId);
        return MemberDetailRespDto.from(memberDetail);
    }
    //수정
    @Transactional
    public void updateMemberDetail(MemberDetailReqDto reqDto) {
        MemberDetail memberDetail = entityManager.find(MemberDetail.class, reqDto.getMemberDetailId());
        memberDetail.setName(reqDto.getName());
        memberDetail.setNickname(reqDto.getNickname());
        memberDetail.setPhoneNumber(reqDto.getPhoneNumber());
        memberDetail.setAge(reqDto.getAge());
        memberDetail.setGender(reqDto.getGender());
        memberDetail.setBirth(reqDto.getBirth());
    }
    //삭제
    public void deleteMemberDetail(Long memberId) {
        memberDetailRepository.deleteMemberDetailByMemberIdEquals(memberId);
    }


}
