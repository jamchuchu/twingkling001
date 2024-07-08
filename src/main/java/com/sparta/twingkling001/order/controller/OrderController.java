package com.sparta.twingkling001.order.controller;

import com.sparta.twingkling001.api.exception.ErrorType;
import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.order.constant.OrderConstants;
import com.sparta.twingkling001.order.constant.OrderState;
import com.sparta.twingkling001.order.dto.request.OrderDetailReqDto;
import com.sparta.twingkling001.order.dto.request.OrderQuantityReqDto;
import com.sparta.twingkling001.order.dto.request.OrderReceiveReqDto;
import com.sparta.twingkling001.order.dto.request.OrderReqDto;
import com.sparta.twingkling001.order.dto.response.OrderDetailRespDto;
import com.sparta.twingkling001.order.dto.response.OrderRespDto;
import com.sparta.twingkling001.order.entity.Order;
import com.sparta.twingkling001.order.service.OrderService;
import com.sparta.twingkling001.product.service.ProductService;
import com.sparta.twingkling001.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderController {
    private final OrderService orderService;
    private final RedisService redisService;

    // 주문 작성 (주문 디테일 까지 다 받아서 작성)
    @Transactional
    @PostMapping("/")
    public ResponseEntity<?> addOrder(@RequestBody OrderReqDto orderReqDto) {
        OrderRespDto response = orderService.addOrder(orderReqDto);
        List<OrderDetailReqDto> orderDetails = orderReqDto.getOrderDetailsList();
        List<OrderDetailRespDto> orderDetailRespDtoList = new ArrayList<>();
        orderDetails.forEach(detail ->{
            try {
                OrderDetailRespDto orderDetailRespDto = orderService.addOrderDetail(response.getOrderId(), detail);
                orderDetailRespDtoList.add(orderDetailRespDto);
            }catch (Exception e){
                log.info(e.getMessage());
            }
        });
        response.setOrderDetails(orderDetailRespDtoList);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
//                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    //주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderRespDto>> getOrder(@PathVariable Long orderId) {
        OrderRespDto response = orderService.getOrder(orderId);
        List<OrderDetailRespDto> orderDetails = orderService.getOrderDetailByOrder(orderId);
        response.setOrderDetails(orderDetails);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    // 멤버 별 주문 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<OrderRespDto>>> getOrderByMember(@PathVariable Long memberId) {
        List<OrderRespDto> response = orderService.getOrderByMember(memberId);
        response.stream().map(reqDto -> {
            List<OrderDetailRespDto> orderDetails = orderService.getOrderDetailByOrder(reqDto.getOrderId());
            reqDto.setOrderDetails(orderDetails);
            return reqDto;
        });
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    // 멤버별, 상태별 주문 조회
    @GetMapping("/{memberId}/state")
    public ResponseEntity<ApiResponse<List<OrderRespDto>>> getOrderByMemberAndState(@PathVariable Long memberId, @RequestParam OrderState state) {
        List<OrderRespDto> response = orderService.getOrderByMemberAndState(memberId, state);
        response.stream().map(reqDto -> {
            List<OrderDetailRespDto> orderDetails = orderService.getOrderDetailByOrder(reqDto.getOrderId());
            reqDto.setOrderDetails(orderDetails);
            return reqDto;
        });
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    // 개별 주문 조회
    @GetMapping("/detail/{orderDetailId}")
    public ResponseEntity<ApiResponse<OrderDetailRespDto>> getOrderDetail(@PathVariable Long orderDetailId) {
        OrderDetailRespDto response = orderService.getOrderDetail(orderDetailId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    // 주문 수정(이름, 전화번호, 받는사람이름)
    @PatchMapping("/{orderId}/receive_info")
    public ResponseEntity<ApiResponse<?>> updateOrderReceiveInfo (@PathVariable Long orderId, @RequestBody OrderReceiveReqDto orderReceiveReqDto){
        orderService.updateOrderReceiveInfo(orderId, orderReceiveReqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //결제: 각 결제 완료  orderId(redis in)
    @PatchMapping("/{orderId}/payment_date")
    public ResponseEntity<ApiResponse<?>> updatePaymentDate(@PathVariable Long orderId, @RequestBody LocalDateTime date){
        orderService.updatePaymentDateAndState(orderId, date);
        redisService.addSetValue(OrderConstants.AWAIT_SHIP, orderId.toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    //주문 디테일 개수, 가격 수정
    @PatchMapping("/{orderDetailId}/quantity")
    public ResponseEntity<ApiResponse<?>> updateOrderDetailQuantity (@PathVariable Long orderDetailId, @RequestBody OrderQuantityReqDto reqDto){
        orderService.updateOrderDetailQuantity(orderDetailId, reqDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

//
//    //배송 완료 시간 수정
//    @PatchMapping("/{orderId}/deliverDate")
//    public ResponseEntity<ApiResponse<?>> updateDeliverDate(@PathVariable Long orderId, @RequestBody LocalDateTime date){
//        orderService.updateDeliverDate(orderId, date);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(ApiResponse.success(SuccessType.SUCCESS));
//    }

    // 주문 삭제(detail 까지 한번에 삭제)
    @DeleteMapping("/{orderId}/cansel")
    public ResponseEntity<ApiResponse<?>> deliteOrder (Long orderId) {
        orderService.deleteOrder(orderId);
//        orderService.deleteOrderDetailByOrderId(orderId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    // 주문 환불(detail 까지 한번에 삭제)
    @DeleteMapping("/{orderId}/refund")
    public ResponseEntity<ApiResponse<?>> refundOrder (Long orderId) {
        orderService.refundOrder(orderId);
        orderService.deleteOrderDetailByOrderId(orderId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }


}
