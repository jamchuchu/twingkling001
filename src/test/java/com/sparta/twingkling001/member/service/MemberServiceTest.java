package com.sparta.twingkling001.member.service;

import com.sparta.twingkling001.api.exception.general.DataNotFoundException;
import com.sparta.twingkling001.like.entity.Like;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    MemberRepository memberRepository;
    @Mock
    MemberDetailRepository memberDetailRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    EntityManager entityManager;

    LocalDateTime now = LocalDateTime.now();

    @InjectMocks
    MemberService memberService;

    @Test
    void addMember() {
        //given
        Long memberId = 1L;
        MemberReqDtoByMail memberBymail = new MemberReqDtoByMail("nick", "nick@email.com", "password" , null);
        Member member = Member.builder()
                .memberId(1L)
                .role(Role.USER)
                .email(memberBymail.getEmail())
                .password(passwordEncoder.encode(memberBymail.getPassword()))
                .createdAt(LocalDateTime.now())
                .deletedYn(false)
                .build();
        //when
        when(memberRepository.save(any(Member.class))).thenReturn(member);
        Long response = memberService.addMember(memberBymail);

        //then
        assertEquals(1L, response);
    }

    @Test
    void getMemberByMemberId() {
        //given
        Long memberId = 1L;
        Member member = new Member(1L, Role.USER, "user@example.com", "password123",now, false);

        //when
        when(memberRepository.findMemberByMemberId(memberId)).thenReturn(Optional.of(member));
        MemberRespDto expected = MemberRespDto.from(member);
        MemberRespDto response = memberService.getMemberByMemberId(memberId);

        //then
        assertEquals(expected.getMemberId(), response.getMemberId());
        assertEquals(expected.getRole(), response.getRole());
        assertEquals(expected.getEmail(), response.getEmail());
        assertEquals(expected.getPassword(), response.getPassword());
        assertEquals(expected.getCreatedAt(), response.getCreatedAt());
    }

    @Test
    void getMemberByEmail() {
        //given
        String email = "user@example.com";
        Member member = new Member(1L, Role.USER, "user@example.com", "password123",now, false);

        //when
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        MemberRespDto expected = MemberRespDto.from(member);
        MemberRespDto response = memberService.getMemberByEmail(email);

        //then
        assertEquals(expected.getMemberId(), response.getMemberId());
        assertEquals(expected.getRole(), response.getRole());
        assertEquals(expected.getEmail(), response.getEmail());
        assertEquals(expected.getPassword(), response.getPassword());
        assertEquals(expected.getCreatedAt(), response.getCreatedAt());
    }

    @Test
    void updateMemberPassword() {
        //given
        Long memberId = 1L;
        String newPassword = "newPassword123";
        Member member = new Member(1L, Role.USER, "user@example.com", "password123", now, false);
        String originPassword = member.getPassword();

        //when
        when(entityManager.find(Member.class, memberId)).thenReturn(member);
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        memberService.updateMemberPassword(memberId, newPassword);

        //then
        assertNotEquals(originPassword, member.getPassword());
        assertEquals(newPassword, member.getPassword());
    }

//    @Test
//    void deleteMember() {
//    }

    @Test
    void checkEmailForm() {
        //given
        String email1 = "user@example.com";
        String email2 = "user";
        String email3 = "@example.com";
        String email4 = "userexample.com";

        //when & then
        assertEquals(true, memberService.checkEmailForm(email1));
        assertEquals(false, memberService.checkEmailForm(email2));
        assertEquals(false, memberService.checkEmailForm(email3));
        assertEquals(false, memberService.checkEmailForm(email4));
    }

    @Test
    void checkDupleSuccess() {
        //given
        String email = "user@example.com";
        Long count = 0L;

        //when
        when(memberRepository.countMemberByEmail(email)).thenReturn(count);

        //then
        assertEquals(true, memberService.checkDuple(email));
    }
    @Test
    void checkDupleFail() {
        //given
        String email = "user@example.com";
        Long count = 1L;

        //when
        when(memberRepository.countMemberByEmail(email)).thenReturn(count);

        //then
        assertEquals(false, memberService.checkDuple(email));
    }

    @Test
    void addMemberDetail() {
        //given
        Long memberId = 1L;
        MemberDetailReqDto reqDto = new MemberDetailReqDto(1L, 1L, "30", LocalDate.of(1993, 1, 1), "Male", "홍길동", "길동이", "010-1234-5678");
        MemberDetail memberDetail = MemberDetail.from(reqDto);
        //when
        when(memberDetailRepository.save(any(MemberDetail.class))).thenReturn(memberDetail);
        Long response = memberService.addMemberDetail(reqDto);

        //then
        assertEquals(memberId, response);
    }

    @Test
    void getMemberDetail() {
        //given
        Long memberId = 1L;
        MemberDetail memberDetail = new MemberDetail(1L, 1L, "홍길동", "길동이", "010-1234-5678", "30", "Male", LocalDate.of(1993, 1, 1));

        //when
        when(memberDetailRepository.getMemberDetailByMemberId(memberId)).thenReturn(memberDetail);
        MemberDetailRespDto expected = MemberDetailRespDto.from(memberDetail);
        MemberDetailRespDto response = memberService.getMemberDetail(memberId);

        //then
        assertEquals(expected.getMemberDetailId(), response.getMemberDetailId());
        assertEquals(expected.getMemberId(), response.getMemberId());
        assertEquals(expected.getAge(), response.getAge());
        assertEquals(expected.getBirth(), response.getBirth());
        assertEquals(expected.getGender(), response.getGender());
        assertEquals(expected.getName(), response.getName());
        assertEquals(expected.getNickname(), response.getNickname());
        assertEquals(expected.getPhoneNumber(), response.getPhoneNumber());
    }

    @Test
    void updateMemberDetail() throws DataNotFoundException {
        //given
        MemberDetailReqDto reqDto = new MemberDetailReqDto(1L, 1L, "99", LocalDate.of(1999, 12, 31), "NONE", "newName", "newNickName", "010-9999-9999");
        MemberDetail memberDetail = new MemberDetail(1L, 1L, "홍길동", "길동이", "010-1234-5678", "30", "Male", LocalDate.of(1993, 1, 1));

        //when
        when(entityManager.find(MemberDetail.class, reqDto.getMemberDetailId())).thenReturn(memberDetail);
        memberService.updateMemberDetail(reqDto);

        //then
        assertEquals(reqDto.getName(), memberDetail.getName());
        assertEquals(reqDto.getNickname(), memberDetail.getNickname());
        assertEquals(reqDto.getPhoneNumber(), memberDetail.getPhoneNumber());
        assertEquals(reqDto.getAge(), memberDetail.getAge());
        assertEquals(reqDto.getGender(), memberDetail.getGender());
        assertEquals(reqDto.getBirth(), memberDetail.getBirth());
    }

//    @Test
//    void deleteMemberDetail() {
//    }
}