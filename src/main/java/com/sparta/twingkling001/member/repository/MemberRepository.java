package com.sparta.twingkling001.member.repository;

import com.sparta.twingkling001.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT COUNT(m.memberId) FROM Member m WHERE m.email = :email")
     Long getMemberSizeByEmail(String email);

     Member getMemberByEmail(String email);
}
