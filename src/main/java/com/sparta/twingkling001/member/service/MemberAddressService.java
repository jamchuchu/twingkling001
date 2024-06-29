package com.sparta.twingkling001.member.service;

import com.sparta.twingkling001.address.dto.request.AddressReqDto;
import com.sparta.twingkling001.address.entity.Address;
import com.sparta.twingkling001.address.repository.AddressRepository;
import com.sparta.twingkling001.member.dto.request.MemberAddressReqDto;
import com.sparta.twingkling001.member.dto.response.MemberAddressRespDto;
import com.sparta.twingkling001.member.entity.MemberAddress;
import com.sparta.twingkling001.member.repository.MemberAddressRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberAddressService {
    private final MemberAddressRepository memberAddressRepository;
    private final AddressRepository addressRepository;
    private final EntityManager entityManager;


    @Transactional
    public Long addMemberAddress(MemberAddressReqDto reqDto) {
        if(reqDto.isPrimary()) {
            MemberAddress mainAddress = memberAddressRepository.findByMemberIdAndIsPrimary(reqDto.getMemberId(), true);
            if(mainAddress != null){
                mainAddress.setPrimary(false);
                deleteMemberSubAddressMoreThanCount(reqDto.getMemberId());
            }
        }
        if(!reqDto.isPrimary()){
            deleteMemberSubAddressMoreThanCount(reqDto.getMemberId());
        }
        //입력 하자마자 주소 더함
        Address address = addressRepository.findAddressByAddressId(reqDto.getAddressId());
        return memberAddressRepository.save(MemberAddress.from(address,reqDto)).getMemberAddressId();
    }

    public List<MemberAddressRespDto> getMemberAddresses(Long memberId){
        return memberAddressRepository.findMemberAddressesByMemberId(memberId).stream()
                .map(MemberAddressRespDto::from).toList();
    }

    @Transactional
    public void updateMemberAddress(Long memberAddressId, Long addressId){
        MemberAddress memberAddress = entityManager.find(MemberAddress.class, memberAddressId);
        memberAddress.getAddress().setDeletedYn(true);
        Address address = addressRepository.findAddressByAddressId(addressId);
        memberAddress.setAddress(address);
    }

    @Transactional
    public void updateMemberAddressUsedAt(Long memberAddressId, MemberAddressReqDto reqDto) {
        MemberAddress memberAddress = entityManager.find(MemberAddress.class, memberAddressId);
        memberAddress.setUsedAt(reqDto.getUsedAt());
    }

    @Transactional
    public void updateMemberAddressPrimary(Long memberAddressId) {
        MemberAddress memberAddress = entityManager.find(MemberAddress.class, memberAddressId);
        memberAddress.setPrimary(false);
    }

    public void deleteMemberAddress(Long memberAddressId){
        memberAddressRepository.deleteMemberAddressByMemberAddressId(memberAddressId);
    }

    //5개 이상이면 삭제
    public void deleteMemberSubAddressMoreThanCount(Long memberId) {
        List<MemberAddress> subAddresses = memberAddressRepository.findMemberAddressesByMemberIdAndIsPrimaryIsFalse(memberId);
        if(subAddresses.size() == 5) {
            Long oldestMemberAddressId = subAddresses.get(0).getMemberAddressId();
            memberAddressRepository.deleteMemberAddressByMemberAddressId(oldestMemberAddressId);
        }
    }

}
