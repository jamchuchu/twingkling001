package com.sparta.twingkling001.order.service;

import com.sparta.twingkling001.api.exception.order.InvalidReturnStatusException;
import com.sparta.twingkling001.api.exception.order.ReturnPeriodExpiredException;
import com.sparta.twingkling001.order.constant.OrderState;
import com.sparta.twingkling001.order.dto.request.OrderDetailReqDto;
import com.sparta.twingkling001.order.dto.request.OrderReceiveReqDto;
import com.sparta.twingkling001.order.dto.request.OrderReqDto;
import com.sparta.twingkling001.order.dto.response.OrderDetailRespDto;
import com.sparta.twingkling001.order.dto.response.OrderRespDto;
import com.sparta.twingkling001.order.entity.Order;
import com.sparta.twingkling001.order.entity.OrderDetail;
import com.sparta.twingkling001.order.repository.OrderDetailRepository;
import com.sparta.twingkling001.order.repository.OrderRepository;
import com.sparta.twingkling001.product.entity.ProductDetail;
import com.sparta.twingkling001.product.repository.ProductDetailRepository;
import com.sparta.twingkling001.product.service.ProductService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    ProductService productService;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderDetailRepository orderDetailRepository;
    @Mock
    ProductDetailRepository productDetailRepository;
    @Mock
    EntityManager entityManager;

    LocalDateTime now = LocalDateTime.now();

    @InjectMocks
    OrderService orderService;

    @Test
    void addOrder() {
        //given
        OrderReqDto reqDto = new OrderReqDto(1L, 1L, 1L, "홍길동", "010-1234-5678",now, now.plusDays(3), now, false, OrderState.ORDER_COMPLETED, 50000L, 3000L, 1L, new ArrayList<>());
        Order order = Order.from(reqDto);

        //when
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        OrderRespDto expected = OrderRespDto.from(order);
        OrderRespDto response = orderService.addOrder(reqDto);

        //then
        assertEquals(expected.getOrderId(), response.getOrderId());
        assertEquals(expected.getMemberId(), response.getMemberId());
        assertEquals(expected.getReceiveAddressId(), response.getReceiveAddressId());
        assertEquals(expected.getReceiveName(), response.getReceiveName());
        assertEquals(expected.getReceivePhoneNumber(), response.getReceivePhoneNumber());
        assertEquals(expected.getPaymentDate(), response.getPaymentDate());
        assertEquals(expected.getDeliverDate(), response.getDeliverDate());
        assertEquals(expected.getCreateAt(), response.getCreateAt());
        assertEquals(expected.getDeletedYn(), response.getDeletedYn());
        assertEquals(expected.getOrderState(), response.getOrderState());
        assertEquals(expected.getTotalPrice(), response.getTotalPrice());
        assertEquals(expected.getDeliverFee(), response.getDeliverFee());
        assertEquals(expected.getPayId(), response.getPayId());
    }

    @Test
    void addOrderDetail() throws Exception {
        // given
        Long orderId = 1L;
        Order order = new Order(1L, 1L, 1L, "홍길동", "010-1234-5678", now, now, now, false, OrderState.ORDER_COMPLETED, 10000L, 3000L, 1L);
        OrderDetailReqDto reqDto = new OrderDetailReqDto(1L, null, 1L, 2L, false, 10000L);
        OrderDetail orderDetail = OrderDetail.from(order, reqDto);
        ProductDetail productDetail = new ProductDetail();
        productDetail.setSaleQuantity(100L);

        // when
        when(entityManager.find(Order.class, orderId)).thenReturn(order);
        when(productDetailRepository.findProductDetailByProductDetailId(reqDto.getProductDetailId())).thenReturn(productDetail);
        when(orderDetailRepository.save(any(OrderDetail.class))).thenReturn(orderDetail);
        doNothing().when(productService).minusProductQuantity(any(Long.class));

        OrderDetailRespDto response = orderService.addOrderDetail(orderId, reqDto);

        // then
        verify(entityManager).find(Order.class, orderId);
        verify(productDetailRepository).findProductDetailByProductDetailId(reqDto.getProductDetailId());
        verify(orderDetailRepository).save(any(OrderDetail.class));
        verify(productService).minusProductQuantity(any(Long.class));

        assertEquals(orderDetail.getOrderDetailId(), response.getOrderDetailId());
        assertEquals(order.getOrderId(), response.getOrder().getOrderId());
        assertEquals(reqDto.getProductDetailId(), response.getProductDetailId());
        assertEquals(reqDto.getQuantity(), response.getQuantity());
        assertEquals(reqDto.getDeletedYn(), response.getDeletedYn());
        assertEquals(reqDto.getPrice(), response.getPrice());
    }

    @Test
    void getOrder() {
        // Given
        Long orderId = 1L;
        Order order = new Order(orderId, 1L, 1L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, OrderState.ORDER_COMPLETED, 50000L, 3000L, 1L);
        when(orderRepository.findOrderByOrderId(orderId)).thenReturn(order);

        // When
        OrderRespDto result = orderService.getOrder(orderId);

        // Then
        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals(order.getMemberId(), result.getMemberId());
        assertEquals(order.getReceiveName(), result.getReceiveName());
        // Add more assertions for other fields

        verify(orderRepository).findOrderByOrderId(orderId);
    }

    @Test
    void getOrderDetailByOrder() {
        // Given
        Long orderId = 1L;
        Order order = new Order(orderId, 1L, 1L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, OrderState.ORDER_COMPLETED, 50000L, 3000L, 1L);
        OrderDetail detail1 = new OrderDetail(1L, order, 1L, 2L, false, 20000L);
        OrderDetail detail2 = new OrderDetail(2L, order, 2L, 1L, false, 30000L);
        List<OrderDetail> orderDetails = Arrays.asList(detail1, detail2);

        when(orderDetailRepository.findOrderDetailsByOrder_OrderIdAndDeletedYnFalse(orderId)).thenReturn(orderDetails);

        // When
        List<OrderDetailRespDto> result = orderService.getOrderDetailByOrder(orderId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(detail1.getOrderDetailId(), result.get(0).getOrderDetailId());
        assertEquals(detail2.getOrderDetailId(), result.get(1).getOrderDetailId());
        // Add more assertions for other fields

        verify(orderDetailRepository).findOrderDetailsByOrder_OrderIdAndDeletedYnFalse(orderId);
    }

    @Test
    void getOrderByMember() {
        // Given
        Long memberId = 1L;
        Order order1 = new Order(1L, memberId, 1L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, OrderState.ORDER_COMPLETED, 50000L, 3000L, 1L);
        Order order2 = new Order(2L, memberId, 2L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, OrderState.ORDER_COMPLETED, 60000L, 3000L, 2L);
        List<Order> orders = Arrays.asList(order1, order2);

        when(orderRepository.findOrdersByMemberId(memberId)).thenReturn(orders);

        // When
        List<OrderRespDto> result = orderService.getOrderByMember(memberId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(order1.getOrderId(), result.get(0).getOrderId());
        assertEquals(order2.getOrderId(), result.get(1).getOrderId());
        // Add more assertions for other fields

        verify(orderRepository).findOrdersByMemberId(memberId);
    }

    @Test
    void getOrderByMemberAndState() {
        // Given
        Long memberId = 1L;
        OrderState orderState = OrderState.ORDER_COMPLETED;
        Order order1 = new Order(1L, memberId, 1L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, orderState, 50000L, 3000L, 1L);
        Order order2 = new Order(2L, memberId, 2L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, orderState, 60000L, 3000L, 2L);
        List<Order> orders = Arrays.asList(order1, order2);

        when(orderRepository.findOrdersByMemberIdAndOrderStateAndDeletedYnFalse(memberId, orderState)).thenReturn(orders);

        // When
        List<OrderRespDto> result = orderService.getOrderByMemberAndState(memberId, orderState);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(order1.getOrderId(), result.get(0).getOrderId());
        assertEquals(order2.getOrderId(), result.get(1).getOrderId());

        verify(orderRepository).findOrdersByMemberIdAndOrderStateAndDeletedYnFalse(memberId, orderState);
    }

    @Test
    void getOrderDetail() {
        // Given
        Long orderDetailId = 1L;
        Order order = new Order(1L, 1L, 1L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, OrderState.ORDER_COMPLETED, 50000L, 3000L, 1L);
        OrderDetail orderDetail = new OrderDetail(orderDetailId, order, 1L, 2L, false, 20000L);

        when(orderDetailRepository.findOrderDetailByOrderDetailId(orderDetailId)).thenReturn(orderDetail);

        // When
        OrderDetailRespDto result = orderService.getOrderDetail(orderDetailId);

        // Then
        assertNotNull(result);
        assertEquals(orderDetailId, result.getOrderDetailId());
        assertEquals(order.getOrderId(), result.getOrder().getOrderId());

        verify(orderDetailRepository).findOrderDetailByOrderDetailId(orderDetailId);
    }

    @Test
    void updateOrderReceiveInfo() {
        // Given
        Long orderId = 1L;
        OrderReceiveReqDto orderReceiveReqDto = new OrderReceiveReqDto(1L, "new Name", "010-9999-9999");
        Order order = new Order(orderId, 1L, 1L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, OrderState.ORDER_COMPLETED, 50000L, 3000L, 1L);

        when(entityManager.find(Order.class, orderId)).thenReturn(order);

        // When
        orderService.updateOrderReceiveInfo(orderId, orderReceiveReqDto);

        // Then
        assertEquals(orderReceiveReqDto.getReceiveName(), order.getReceiveName());
        assertEquals(orderReceiveReqDto.getReceivePhoneNumber(), order.getReceivePhoneNumber());
        assertEquals(orderReceiveReqDto.getReceiveAddressId(), order.getReceiveAddressId());

        verify(entityManager).find(Order.class, orderId);
    }

    @Test
    void updateOrderReceiveInfo_OrderNotFound() {
        // Given
        Long orderId = 1L;
        OrderReceiveReqDto orderReceiveReqDto = new OrderReceiveReqDto(1L, "new Name", "010-9999-9999");

        when(entityManager.find(Order.class, orderId)).thenReturn(null);

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            orderService.updateOrderReceiveInfo(orderId, orderReceiveReqDto);
        });

        verify(entityManager).find(Order.class, orderId);
    }
    @Test
    void updateOrderStateToShipped_Success() {
        // Given
        Long orderId = 1L;
        Order order = new Order(orderId, 1L, 1L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, OrderState.ORDER_COMPLETED, 50000L, 3000L, 1L);
        when(entityManager.find(Order.class, orderId)).thenReturn(order);

        // When
        orderService.updateOrderStateToShipped(orderId);

        // Then
        assertEquals(OrderState.SHIPPED, order.getOrderState());
        verify(entityManager).find(Order.class, orderId);
    }

    @Test
    void updateOrderStateToShipped_OrderNotFound() {
        // Given
        Long orderId = 1L;
        when(entityManager.find(Order.class, orderId)).thenReturn(null);

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            orderService.updateOrderStateToShipped(orderId);
        });
        verify(entityManager).find(Order.class, orderId);
    }

    @Test
    void updatePaymentDateAndState_Success() {
        // Given
        Long orderId = 1L;
        LocalDateTime paymentDate = LocalDateTime.now();
        Order order = new Order(orderId, 1L, 1L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, OrderState.ORDER_COMPLETED, 50000L, 3000L, 1L);
        when(entityManager.find(Order.class, orderId)).thenReturn(order);

        // When
        orderService.updatePaymentDateAndState(orderId, paymentDate);

        // Then
        assertEquals(paymentDate, order.getPaymentDate());
        assertEquals(OrderState.PAY_COMPLETED, order.getOrderState());
        verify(entityManager).find(Order.class, orderId);
    }

    @Test
    void updatePaymentDateAndState_OrderNotFound() {
        // Given
        Long orderId = 1L;
        LocalDateTime paymentDate = LocalDateTime.now();
        when(entityManager.find(Order.class, orderId)).thenReturn(null);

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            orderService.updatePaymentDateAndState(orderId, paymentDate);
        });
        verify(entityManager).find(Order.class, orderId);
    }

    @Test
    void updateDeliverDateAndState_Success() {
        // Given
        Long orderId = 1L;
        LocalDateTime deliverDate = LocalDateTime.now();
        Order order = new Order(orderId, 1L, 1L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, OrderState.PAY_COMPLETED, 50000L, 3000L, 1L);
        when(entityManager.find(Order.class, orderId)).thenReturn(order);

        // When
        orderService.updateDeliverDateAndState(orderId, deliverDate);

        // Then
        assertEquals(deliverDate, order.getDeliverDate());
        assertEquals(OrderState.DELIVERY_COMPLETED, order.getOrderState());
        verify(entityManager).find(Order.class, orderId);
    }

    @Test
    void updateDeliverDateAndState_OrderNotFound() {
        // Given
        Long orderId = 1L;
        LocalDateTime deliverDate = LocalDateTime.now();
        when(entityManager.find(Order.class, orderId)).thenReturn(null);

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            orderService.updateDeliverDateAndState(orderId, deliverDate);
        });
        verify(entityManager).find(Order.class, orderId);
    }
    @Test
    void deleteOrder_PayCompletedState() {
        // Given
        Long orderId = 1L;
        Order order = new Order(orderId, 1L, 1L, "홍길동", "010-1234-5678", LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), false, OrderState.PAY_COMPLETED, 50000L, 3000L, 1L);
        when(entityManager.find(Order.class, orderId)).thenReturn(order);

        // When
        orderService.deleteOrder(orderId);

        // Then
        assertTrue(order.getDeletedYn());
        assertEquals(OrderState.CANSEL, order.getOrderState());
        verify(entityManager).find(Order.class, orderId);
    }

    @Test
    void deleteOrder_OrderNotFound() {
        // Given
        Long orderId = 1L;
        when(entityManager.find(Order.class, orderId)).thenReturn(null);

        // When & Then
        assertThrows(IllegalStateException.class, () -> orderService.deleteOrder(orderId));
        verify(entityManager).find(Order.class, orderId);
    }

    @Test
    void deleteOrderDetailByOrderId_Success() throws Exception {
        // Given
        Long orderId = 1L;
        OrderDetail detail1 = new OrderDetail(1L, null, 1L, 2L, false, 20000L);
        OrderDetail detail2 = new OrderDetail(2L, null, 2L, 1L, false, 30000L);
        List<OrderDetail> details = Arrays.asList(detail1, detail2);

        when(orderDetailRepository.findOrderDetailsByOrder_OrderIdAndDeletedYnFalse(orderId)).thenReturn(details);

        // When
        orderService.deleteOrderDetailByOrderId(orderId);

        // Then
        assertTrue(detail1.getDeletedYn());
        assertTrue(detail2.getDeletedYn());
        verify(orderDetailRepository).findOrderDetailsByOrder_OrderIdAndDeletedYnFalse(orderId);
        verify(productService, times(2)).plusProductQuantity(anyLong());
    }

    @Test
    void refundOrder_Success() throws Exception {
        // Given
        Long orderId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(orderId, 1L, 1L, "홍길동", "010-1234-5678", now.minusDays(1), now.minusHours(12), now, false, OrderState.DELIVERY_COMPLETED, 50000L, 3000L, 1L);
        when(entityManager.find(Order.class, orderId)).thenReturn(order);

        // When
        orderService.refundOrder(orderId);

        // Then
        assertTrue(order.getDeletedYn());
        assertEquals(OrderState.CANSEL, order.getOrderState());
        verify(entityManager).find(Order.class, orderId);
    }

    @Test
    void refundOrder_ReturnPeriodExpired() {
        // Given
        Long orderId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(orderId, 1L, 1L, "홍길동", "010-1234-5678", now.minusDays(2), now.minusDays(1), now, false, OrderState.DELIVERY_COMPLETED, 50000L, 3000L, 1L);
        when(entityManager.find(Order.class, orderId)).thenReturn(order);

        // When & Then
        assertThrows(ReturnPeriodExpiredException.class, () -> orderService.refundOrder(orderId));
        verify(entityManager).find(Order.class, orderId);
    }

    @Test
    void refundOrder_InvalidStatus() {
        // Given
        Long orderId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(orderId, 1L, 1L, "홍길동", "010-1234-5678", now.minusDays(1), now.minusHours(12), now, false, OrderState.SHIPPED, 50000L, 3000L, 1L);
        when(entityManager.find(Order.class, orderId)).thenReturn(order);

        // When & Then
        assertThrows(InvalidReturnStatusException.class, () -> orderService.refundOrder(orderId));
        verify(entityManager).find(Order.class, orderId);
    }
}