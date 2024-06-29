package com.sparta.twingkling001.address.service;

import com.sparta.twingkling001.address.dto.request.AddressReqDto;
import com.sparta.twingkling001.address.entity.Address;
import com.sparta.twingkling001.address.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public Long addAddressNumber(AddressReqDto addressReqDto){
        return addressRepository.save(Address.from(addressReqDto)).getAddressId();
    }
    public Address getAddress(Long addressId){
        return addressRepository.findAddressByAddressId(addressId);
    }


}
