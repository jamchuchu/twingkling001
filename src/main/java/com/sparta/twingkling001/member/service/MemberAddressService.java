package com.sparta.twingkling001.member.service;

import com.sparta.twingkling001.member.dto.request.MemberAddressReqDto;
import com.sparta.twingkling001.member.dto.response.MemberAddressRespDto;
import com.sparta.twingkling001.member.entity.MemberAddress;
import com.sparta.twingkling001.member.repository.MemberAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberAddressService {
    private final MemberAddressRepository memberAddressRepository;

    public Long addMemberAddress(MemberAddressReqDto reqDto) {
        MemberAddress memberAddress = MemberAddress.builder()
                .memberId(reqDto.getMemberId())
                .addressId(reqDto.getAddressId())
                .isPrimary(reqDto.isPrimary())
                .build();
        return memberAddressRepository.save(memberAddress).getMemberAddressId();
    }

    public List<MemberAddressRespDto> getMemberAddresses(Long memberId){
        return memberAddressRepository.getMemberAddresses(memberId);
    }

    public Long updateMemberAddress(Long memberAddressId, MemberAddressReqDto reqDto) {
        return memberAddressRepository.updateMemberAddress(memberAddressId, reqDto).getMemberAddressId();
    }

    public Long updateMemberAddressLevel(Long memberId, Long memberAddressId) {
        MemberAddress mainAddress = memberAddressRepository.getMemberAddressByMemberIdAndIsPrimary(memberId);
        if(mainAddress != null){
            memberAddressRepository.updateSubByMemberId(memberId);
            deleteMemberSubAddressMoreThanCount(memberId);
        }
        return memberAddressRepository.updatePrimaryByMemberAddressId(memberAddressId).getMemberAddressId();
    }

    public void deleteMemberAddress(Long memberAddressId){
        memberAddressRepository.deleteMemberAddressByAddressId(memberAddressId);
    }

    //5개 이상이면 삭제
    public void deleteMemberSubAddressMoreThanCount(Long memberId) {
        List<MemberAddress> subAddresses = memberAddressRepository.getMemberSubAddresses(memberId);
        if(subAddresses.size() == 5) {
            Long oldestMemberAddressId = subAddresses.get(0).getMemberAddressId();
            memberAddressRepository.deleteMemberAddressByAddressId(oldestMemberAddressId);
        }
    }

}
