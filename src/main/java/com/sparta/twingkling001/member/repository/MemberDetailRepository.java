package com.sparta.twingkling001.member.repository;

import com.sparta.twingkling001.member.dto.response.MemberDetailRespDto;
import com.sparta.twingkling001.member.entity.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE MemberDetail md SET md.age = :#{#memberDetail.age}, md.birth = :#{#memberDetail.birth}, md.gender = :#{#memberDetail.gender}, md.name = :#{#memberDetail.name}, md.nickname = :#{#memberDetail.nickname}, md.phoneNumber = :#{#memberDetail.phoneNumber} WHERE md.memberId = :#{#memberDetail.memberId}")
    void updateMemberDetail(@Param("memberDetail") MemberDetail memberDetail);

    void deleteMemberDetailByMemberIdEquals(Long memberId);

    MemberDetail getMemberDetailByMemberId(@Param("memberId") long memberId);
}
