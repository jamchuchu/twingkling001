package com.sparta.twingkling001.order.service;

import com.sparta.twingkling001.api.exception.general.DataNotFoundException;
import com.sparta.twingkling001.api.exception.product.NoStockException;
import com.sparta.twingkling001.api.exception.product.UnExpectedLockException;
import com.sparta.twingkling001.order.constant.OrderState;
import com.sparta.twingkling001.order.dto.request.OrderQuantityReqDto;
import com.sparta.twingkling001.order.dto.request.OrderReceiveReqDto;
import com.sparta.twingkling001.order.dto.request.OrderDetailReqDto;
import com.sparta.twingkling001.order.dto.request.OrderReqDto;
import com.sparta.twingkling001.order.dto.response.OrderDetailRespDto;
import com.sparta.twingkling001.order.dto.response.OrderRespDto;
import com.sparta.twingkling001.order.entity.Order;
import com.sparta.twingkling001.order.entity.OrderDetail;
import com.sparta.twingkling001.order.repository.OrderDetailRepository;
import com.sparta.twingkling001.order.repository.OrderRepository;
import com.sparta.twingkling001.product.repository.ProductDetailRepository;
import com.sparta.twingkling001.product.service.ProductService;
import com.sparta.twingkling001.redis.RedisService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductService productService;

    private final OrderRepository  orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final EntityManager entityManager;
    private final RedisService redisService;
    private final RedisLockRegistry redisLockRegistry;
    private final AsyncOrderProcessor asyncOrderProcessor;




    public OrderRespDto addOrder(OrderReqDto orderReqDto) {
        Order order = Order.from(orderReqDto);
        return OrderRespDto.from(orderRepository.save(order));
    }

    @Transactional
    public OrderDetailRespDto addOrderDetail(Long orderId, OrderDetailReqDto reqDto) throws Exception {
        Order order = entityManager.find(Order.class, orderId);
        if(order == null){
            throw new DataNotFoundException();
        }
        if(productDetailRepository.findProductDetailByProductDetailId(reqDto.getProductDetailId()).getSaleQuantity() == 0){
            throw new NoStockException();
        }

        //redis 입력
        OrderDetail detail = OrderDetail.from(order, reqDto);
        OrderDetailRespDto respDto =  OrderDetailRespDto.from(detail);

        asyncOrderProcessor.processOrderDetailAsync(reqDto, detail);
        return respDto;
    }

//
//
//
//    //5초에 한번 실행
//    @Scheduled(fixedRate = 10000)
//    @Transactional
//    public void processAccumulateOrder() {
//        try {
//            Map<String, String> orders = getAccumulateOrders();
//            if (!orders.isEmpty()) {
//                updateQuantityByAccumulated(orders);
//            }
//        } catch (Exception e) {
//            // 로그 기록
//            throw new RuntimeException("Failed to process accumulated orders", e);
//        }
//    }
//
//    public Map<String, String> getAccumulateOrders() throws Exception {
//        Lock lock = redisLockRegistry.obtain("order-update-lock");
//        lock.lock();
//        try {
//            Map<String, String> orders = redisService.getAllHashOps("order");
//            if (orders != null && !orders.isEmpty()) {
//                redisService.deleteValues("order");
//                return orders;
//            }
//            return Collections.emptyMap();
//        } catch (Exception e) {
//            throw new UnExpectedLockException("Failed to get accumulated orders: " + e.getMessage());
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    @Transactional
//    public void updateQuantityByAccumulated(Map<String, String> orders) {
//        orders.forEach((k, v) -> {
//            try {
//                updateSingleProductQuantity(Long.parseLong(k), Long.parseLong(v));
//            } catch (Exception e) {
//                // 로그 기록
//                throw new RuntimeException("Failed to update quantity for product " + k, e);
//            }
//        });
//    }
//
//    @Transactional
//    public void updateSingleProductQuantity(Long productDetailId, Long quantity) throws Exception {
//        productService.minusProductQuantity(productDetailId, quantity);
//        Long saleQuantity = productDetailRepository.findSaleQuantityByProductDetailId(productDetailId).getSaleQuantity();
//        redisService.setHashOps("stock", Map.of(String.valueOf(productDetailId), String.valueOf(saleQuantity)));
//    }


    public OrderRespDto getOrder(Long orderId) {
        return OrderRespDto.from(orderRepository.findOrderByOrderId(orderId));
    }

    public List<OrderDetailRespDto> getOrderDetailByOrder(Long orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailsByOrder_OrderIdAndDeletedYnFalse(orderId);
        return orderDetails.stream().map(OrderDetailRespDto::from).toList();
    }

    public List<OrderRespDto> getOrderByMember(Long memberId) {
        List<Order> orders = orderRepository.findOrdersByMemberId(memberId);
        return orders.stream().map(OrderRespDto::from).toList();
    }


    public List<OrderRespDto> getOrderByMemberAndState(Long memberId, OrderState orderState) {
        List<Order> orders = orderRepository.findOrdersByMemberIdAndOrderStateAndDeletedYnFalse(memberId, orderState);
        return orders.stream().map(OrderRespDto::from).toList();
    }

    public OrderDetailRespDto getOrderDetail(Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findOrderDetailByOrderDetailId(orderDetailId);
        return OrderDetailRespDto.from(orderDetail);
    }

    @Transactional
    public void updateOrderReceiveInfo(Long orderId, OrderReceiveReqDto orderReceiveReqDto) {
        Order order = entityManager.find(Order.class, orderId);
        if(order == null){
            throw new IllegalStateException("Order not found");
        }
        order.setReceiveName(orderReceiveReqDto.getReceiveName());
        order.setReceivePhoneNumber(orderReceiveReqDto.getReceivePhoneNumber());
        order.setReceiveAddressId(orderReceiveReqDto.getReceiveAddressId());
    }

    @Transactional
    public void updateOrderStateToShipped(Long orderId){
        Order order = entityManager.find(Order.class, orderId);
        if(order == null){
            throw new IllegalStateException("Order not found");
        }
        order.setOrderState(OrderState.SHIPPED);
    }

    @Transactional
    public void updatePaymentDateAndState(Long orderId, LocalDateTime date) {
        Order order = entityManager.find(Order.class, orderId);
        if(order == null){
            throw new IllegalStateException("Order not found");
        }
        order.setPaymentDate(date);
        order.setOrderState(OrderState.PAY_COMPLETED);
    }


    @Transactional
    public void updateDeliverDateAndState(Long orderId, LocalDateTime date) {
        Order order = entityManager.find(Order.class, orderId);
        order.setDeliverDate(date);
        order.setOrderState(OrderState.DELIVERY_COMPLETED);
    }



    @Transactional
    public void updateOrderDetailQuantity(Long orderDetailId, OrderQuantityReqDto reqDto) {
        OrderDetail orderDetail = entityManager.find(OrderDetail.class, orderDetailId);
        if(orderDetail == null){
            throw new IllegalStateException("Order not found");
        }
        orderDetail.setQuantity(reqDto.getQuantity());
        orderDetail.setPrice(reqDto.getPrice());
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = entityManager.find(Order.class, orderId);
        if(order == null){
            throw new IllegalStateException("Order not found");
        }
        if(order.getOrderState().equals(OrderState.PAY_COMPLETED) || order.getOrderState().equals(OrderState.PREPARING_FOR_SHIPMENT)) {
            order.setDeletedYn(true);
            order.setOrderState(OrderState.CANSEL);
        }
    }

    @Transactional
    public void deleteOrderDetailByOrderId(Long orderId) throws Exception {
        List<OrderDetail> details = orderDetailRepository.findOrderDetailsByOrder_OrderIdAndDeletedYnFalse(orderId);
        details.forEach(detail -> {
            try {
                productService.plusProductQuantity(detail.getProductDetailId());
            } catch (DataNotFoundException e) {
                throw new RuntimeException(e);
            }
            detail.setDeletedYn(true);
        });
    }

    @Transactional
    public void refundOrder(Long orderId){
        Order order = entityManager.find(Order.class, orderId);
        if(order == null){
            throw new IllegalStateException("Order not found");
        }
        Duration duration = Duration.between(order.getDeliverDate(), LocalDateTime.now());
        if (duration.toHours() >= 24) {
            throw new RuntimeException("반품 가능 기간이 지났습니다");
        }
        if(order.getOrderState().equals(OrderState.DELIVERY_COMPLETED)){
            order.setDeletedYn(true);
            order.setOrderState(OrderState.CANSEL);
            return;
        }
        throw new RuntimeException("반품 가능 상태가 아닙니다.");
    }

    // convertToStringMap 메서드 (RedisService 클래스 내부 또는 유틸리티 클래스에 위치)
    private Map<String, String> convertToStringMap(Map<Long, Long> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> entry.getValue().toString()
                ));
    }

}
