package com.sparta.twingkling001.member.repository;

import com.sparta.twingkling001.member.dto.request.MemberAddressReqDto;
import com.sparta.twingkling001.member.dto.response.MemberAddressRespDto;
import com.sparta.twingkling001.member.entity.Member;
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
    List<MemberAddress> findMemberAddressesByMemberId(Long memberId);
    List<MemberAddress> findMemberAddressesByMemberIdAndIsPrimaryIsFalse(Long memberId);
    MemberAddress findByMemberIdAndIsPrimary(Long memberId, boolean isPrimary);
    void deleteMemberAddressByAddressId(Long memberAddressId);
}
