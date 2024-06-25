package com.sparta.twingkling001.member.repository;

import com.sparta.twingkling001.member.dto.request.MemberAddressReqDto;
import com.sparta.twingkling001.member.dto.response.MemberAddressRespDto;
import com.sparta.twingkling001.member.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
    List<MemberAddressRespDto> getMemberAddressesByMemberId(Long memberId);

    @Query("SELECT ma FROM MemberAddress ma WHERE ma.memberId = :memberId AND ma.isPrimary = false ORDER BY ma.usedAt")
    List<MemberAddress> getMemberSubAddresses(@Param("memberId") Long memberId);

    MemberAddress findByMemberIdAndIsPrimary(Long memberId, boolean isPrimary);

    @Modifying
    @Transactional
    @Query("UPDATE MemberAddress ma SET ma.addressId = :#{#reqDto.addressId} WHERE ma.memberAddressId = :memberAddressId")
    void updateMemberAddress(@Param("memberAddressId") Long memberAddressId, @Param("reqDto") MemberAddressReqDto reqDto);

    @Modifying
    @Transactional
    @Query("UPDATE MemberAddress ma SET ma.isPrimary = true WHERE ma.memberAddressId = :memberAddressId")
    void updatePrimaryByMemberAddressId(@Param("memberAddressId") Long memberAddressId);

    @Modifying
    @Transactional
    @Query("UPDATE MemberAddress ma SET ma.isPrimary = false WHERE ma.memberId = :memberId")
    void updateMainToSubByMemberId(@Param("memberId") Long memberId);

    void deleteMemberAddressByAddressId(Long memberAddressId);
}
