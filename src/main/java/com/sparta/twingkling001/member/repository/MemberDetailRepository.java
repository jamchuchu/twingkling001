package com.sparta.twingkling001.member.repository;

import com.sparta.twingkling001.member.dto.response.MemberDetailRespDto;
import com.sparta.twingkling001.member.entity.MemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberDetailRepository extends JpaRepository<MemberDetail, Long> {

    @Query("UPDATE MemberDetail md SET md.age = :#{#memberDetail.age}, md.birth = :#{#memberDetail.birth}, md.gender = :#{#memberDetail.gender}, md.name = :#{#memberDetail.name}, md.nickname = :#{#memberDetail.nickname}, md.phoneNumber = :#{#memberDetail.phoneNumber} WHERE md.memberId = :#{#memberDetail.memberId}")
    public MemberDetail update(MemberDetail memberDetail);

    @Query("DELETE FROM MemberDetail md WHERE md.memberId = :memberId")
    public void delete(Long memberId);

    @Query("SELECT com.sparta.twingkling001.member.dto.response.MemberDetailRespDto FROM MemberDetail md WHERE md.memberId = :memberId")
    MemberDetailRespDto getMemberDetail(long memberId);
}
