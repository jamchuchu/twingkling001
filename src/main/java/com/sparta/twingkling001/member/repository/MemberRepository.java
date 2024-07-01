package com.sparta.twingkling001.member.repository;

import com.sparta.twingkling001.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByMemberId(long memberId);
    Optional<Member> findByEmail(String email);
    Long countMemberByEmail(String email);
    Optional<Member> getMemberByEmail(String email);
}
