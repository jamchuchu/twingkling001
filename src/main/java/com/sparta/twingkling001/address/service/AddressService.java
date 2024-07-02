package com.sparta.twingkling001.address.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sparta.twingkling001.address.dto.request.AddressReqDto;
import com.sparta.twingkling001.address.dto.response.AddressRespDto;
import com.sparta.twingkling001.address.dto.response.PublicRespDto;
import com.sparta.twingkling001.address.entity.Address;
import com.sparta.twingkling001.address.repository.AddressRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final ObjectMapper objectMapper;
    private final EntityManager entityManager;

    @Value("${address.key}")
    private String CONFIRM_KEY;

    public PublicRespDto getPublicAddressAddress(String keyword, long currentPage, long countPerPage) throws Exception {
        String resultType = "json";

        // OPEN API 호출 URL 정보 설정
        String apiUrl = "https://business.juso.go.kr/addrlink/addrLinkApi.do?currentPage="+currentPage+"&countPerPage="+countPerPage+"&keyword="+ URLEncoder.encode(keyword,"UTF-8")+"&confmKey="+CONFIRM_KEY+"&resultType="+resultType;
        URL url = new URL(apiUrl);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
        PublicRespDto datas = objectMapper.readValue(br, PublicRespDto.class);
        br.close();
        return datas;
    }


    public AddressRespDto addAddress(AddressReqDto addressReqDto){
        Address address = addressRepository.save(Address.from(addressReqDto));
        return AddressRespDto.from(address);
    }

    public AddressRespDto getAddress(Long addressId){
        Address address = addressRepository.findAddressByAddressId(addressId);
        return AddressRespDto.from(address);
    }

    @Transactional
    public void deleteAddress(long addressId) {
        Address address = entityManager.find(Address.class, addressId);
        if(address == null){
            throw new NullPointerException("해당 주소가 없습니다");
        }
        if(address.getDeletedYn()){
            throw new IllegalArgumentException("이미 삭제된 주소 입니다");
        }
        address.setDeletedYn(true);
    }
}
