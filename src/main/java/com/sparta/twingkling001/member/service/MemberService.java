package com.sparta.twingkling001.member.service;


import com.sparta.twingkling001.login.token.JwtToken;
import com.sparta.twingkling001.login.token.JwtTokenProvider;
import com.sparta.twingkling001.member.dto.request.MemberDetailReqDto;
import com.sparta.twingkling001.member.dto.request.MemberReqDtoByMail;
import com.sparta.twingkling001.member.dto.response.MemberDetailRespDto;

import com.sparta.twingkling001.member.entity.Member;
import com.sparta.twingkling001.member.entity.MemberDetail;
import com.sparta.twingkling001.member.entity.Role;
import com.sparta.twingkling001.member.repository.MemberDetailRepository;
import com.sparta.twingkling001.member.repository.MemberRepository;
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

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtToken signIn(String email, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        return jwtToken;
    }


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
