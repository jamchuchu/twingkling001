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
        if(reqDto.isPrimary()) {
            MemberAddress mainAddress = memberAddressRepository.findByMemberIdAndIsPrimary(reqDto.getMemberId(), true);
            if(mainAddress != null){
                memberAddressRepository.updateMainToSubByMemberId(reqDto.getMemberId());
                deleteMemberSubAddressMoreThanCount(reqDto.getMemberId());
            }
        }
        if(!reqDto.isPrimary()){
            deleteMemberSubAddressMoreThanCount(reqDto.getMemberId());
        }
        return memberAddressRepository.save(memberAddress).getMemberAddressId();
    }

    public List<MemberAddressRespDto> getMemberAddresses(Long memberId){
        return memberAddressRepository.getMemberAddressesByMemberId(memberId);
    }

    public void updateMemberAddress(Long memberAddressId, MemberAddressReqDto reqDto) {
        memberAddressRepository.updateMemberAddress(memberAddressId, reqDto);
    }

    // 메인이 있으면 서브로 내리고 원하는 것 메인 추가
    public void updateMemberAddressLevel(Long memberId, Long memberAddressId) {
        MemberAddress mainAddress = memberAddressRepository.findByMemberIdAndIsPrimary(memberId, true);
        if(mainAddress != null){
            memberAddressRepository.updateMainToSubByMemberId(memberId);
            deleteMemberSubAddressMoreThanCount(memberId);
        }
        memberAddressRepository.updatePrimaryByMemberAddressId(memberAddressId);
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
