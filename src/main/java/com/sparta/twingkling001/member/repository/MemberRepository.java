package com.sparta.twingkling001.member.repository;

import com.sparta.twingkling001.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT COUNT(m.memberId) FROM Member m WHERE m.email = :email")
    public Long getMemberSizeByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.email = :email")
    public Member getMemberByEmail(String email);
}
