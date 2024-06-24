package com.sparta.twingkling001.member.repository;

import com.sparta.twingkling001.member.dto.request.MemberAddressReqDto;
import com.sparta.twingkling001.member.dto.response.MemberAddressRespDto;
import com.sparta.twingkling001.member.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
    @Query ("SELECT com.sparta.twingkling001.member.dto.response.MemberAddressRespDto from MemberAddress where memberId = :memberId")
    List<MemberAddressRespDto> getMemberAddresses(Long memberId);

    @Query("Select ma from MemberAddress ma where ma.memberId = :memberId and ma.isPrimary = false order by ma.usedAt")
    List<MemberAddress> getMemberSubAddresses(Long memberId);

    MemberAddress getMemberAddressByMemberIdAndIsPrimary(Long memberId);

    @Query("UPDATE MemberAddress ma SET ma.addressId = :#{#reqDto.addressId} WHERE ma.memberAddressId = :memberAddressId")
    MemberAddress updateMemberAddress(Long memberAddressId, MemberAddressReqDto reqDto);

    //sub -> main
    @Query("update MemberAddress ma set ma.isPrimary = true where ma.memberAddressId = :memberAddressId")
    MemberAddress updatePrimaryByMemberAddressId(Long memberAddressId);

    //main -> sub
    @Query("update MemberAddress ma set ma.isPrimary = false where ma.memberId = :memberId")
    MemberAddress updateSubByMemberId(Long memberId);

    void deleteMemberAddressByAddressId(Long memberAddressId);



}
