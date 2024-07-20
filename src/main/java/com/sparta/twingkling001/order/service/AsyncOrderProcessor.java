package com.sparta.twingkling001.order.service;

import com.sparta.twingkling001.api.exception.product.NoStockException;
import com.sparta.twingkling001.order.dto.request.OrderDetailReqDto;
import com.sparta.twingkling001.order.entity.Order;
import com.sparta.twingkling001.order.entity.OrderDetail;
import com.sparta.twingkling001.order.repository.OrderDetailRepository;
import com.sparta.twingkling001.product.repository.ProductDetailRepository;
import com.sparta.twingkling001.product.service.ProductService;
import com.sparta.twingkling001.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncOrderProcessor {
    private final ProductDetailRepository productDetailRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;
    private final RedisService redisService;

    @Async
    @Transactional
    public void processOrderDetailAsync(OrderDetailReqDto reqDto, OrderDetail detail) throws Exception {
        Long orderDetailId = 0L;
        try {
            productService.minusProductQuantity(detail.getProductDetailId(), reqDto.getQuantity());
            orderDetailId = orderDetailRepository.save(detail).getOrderDetailId();
        } catch (Exception e) {
//             보상 트랜젝션
//            productService.plusProductQuantity(detail.getProductDetailId(), reqDto.getQuantity());
            orderDetailRepository.removeOrderDetailByOrOrderDetailId(orderDetailId);
            throw new NoStockException();
        }
    }
}

//    @Async
//    @Transactional
//    public void processOrderDetailAsync( OrderDetailReqDto reqDto, OrderDetail detail) {
//        try {
//            // DB에 저장
//            orderDetailRepository.save(detail);
//            // Redis 처리
//            saveQuantityInRedis(reqDto);
//        } catch (Exception e) {
//            // 로그 기록
//            log.info(e.getMessage());
//            // 보상 트랜젝션
//            orderDetailRepository.removeOrderDetailByOrOrderDetailId(detail.getOrderDetailId());
//        }
//    }
//
//    private void saveQuantityInRedis(OrderDetailReqDto reqDto) throws NoStockException {
//
//        String stockKey = String.valueOf(reqDto.getProductDetailId());
//        String currentStockStr = redisService.getHashOps("stock", stockKey);
//        Long stock = currentStockStr.isEmpty() ?
//                productDetailRepository.findSaleQuantityByProductDetailId(reqDto.getProductDetailId()).getSaleQuantity() :
//                Long.parseLong(currentStockStr);
//
//        String orderKey = "order";
//        String orderedQuantityStr = redisService.getHashOps(orderKey, stockKey);
//        Long orderedQuantity = orderedQuantityStr.isEmpty()?  0L : Long.parseLong(orderedQuantityStr);
//
//
//        if (reqDto.getQuantity() > stock - orderedQuantity) {
//            throw new NoStockException();
//        }
//
//        redisService.setHashOps(orderKey, Map.of(stockKey, String.valueOf(orderedQuantity + reqDto.getQuantity())));
//    }
//}