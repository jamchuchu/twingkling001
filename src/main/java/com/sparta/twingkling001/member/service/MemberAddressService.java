package com.sparta.twingkling001.member.service;

import com.sparta.twingkling001.member.dto.request.MemberAddressReqDto;
import com.sparta.twingkling001.member.dto.response.MemberAddressRespDto;
import com.sparta.twingkling001.member.entity.MemberAddress;
import com.sparta.twingkling001.member.repository.MemberAddressRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberAddressService {
    private final MemberAddressRepository memberAddressRepository;
    private final EntityManager entityManager;


    public Long addMemberAddress(MemberAddressReqDto reqDto) {
        MemberAddress memberAddress = entityManager.find(MemberAddress.class, reqDto.getMemberAddressId());
        if(reqDto.isPrimary()) {
            MemberAddress mainAddress = memberAddressRepository.findByMemberIdAndIsPrimary(reqDto.getMemberId(), true);
            if(mainAddress != null){
                memberAddress.setPrimary(false);
                deleteMemberSubAddressMoreThanCount(reqDto.getMemberId());
            }
        }
        if(!reqDto.isPrimary()){
            deleteMemberSubAddressMoreThanCount(reqDto.getMemberId());
        }
        return memberAddressRepository.save(memberAddress).getMemberAddressId();
    }

    public List<MemberAddressRespDto> getMemberAddresses(Long memberId){
        return memberAddressRepository.findMemberAddressesByMemberId(memberId).stream()
                .map(MemberAddressRespDto::from).toList();
    }

    public void updateMemberAddress(Long memberAddressId, MemberAddressReqDto reqDto) {
        MemberAddress memberAddress = entityManager.find(MemberAddress.class, memberAddressId);
        memberAddress.setPrimary(reqDto.isPrimary());
        memberAddress.setUsedAt(reqDto.getUsedAt());
    }

    public void deleteMemberAddress(Long memberAddressId){
        memberAddressRepository.deleteMemberAddressByAddressId(memberAddressId);
    }

    //5개 이상이면 삭제
    public void deleteMemberSubAddressMoreThanCount(Long memberId) {
        List<MemberAddress> subAddresses = memberAddressRepository.findMemberAddressesByMemberIdAndIsPrimaryIsFalse(memberId);
        if(subAddresses.size() == 5) {
            Long oldestMemberAddressId = subAddresses.get(0).getMemberAddressId();
            memberAddressRepository.deleteMemberAddressByAddressId(oldestMemberAddressId);
        }
    }

}
