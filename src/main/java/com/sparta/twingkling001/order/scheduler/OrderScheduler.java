package com.sparta.twingkling001.order.scheduler;

import com.sparta.twingkling001.order.constant.OrderConstants;
import com.sparta.twingkling001.order.constant.OrderState;
import com.sparta.twingkling001.order.service.OrderService;
import com.sparta.twingkling001.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OrderScheduler {
    private final RedisService redisService;
    private final OrderService orderService;


    // 매일 오후 4시 배송 중에서 배송완료로 변경
    @Scheduled(cron = OrderConstants.STATE_CHANGE_TIME)
    public void scheduleChangeStateToDelivered() {
        Set<String> set = redisService.getSetValues(OrderConstants.SHIPPED);
        LocalDateTime now = LocalDateTime.now().withHour(16).withMinute(0).withSecond(0).withNano(0);
        for (String o : set) {
            orderService.updateDeliverDateAndState(Long.parseLong( o), now);
        }
        redisService.deleteValues(OrderConstants.SHIPPED);
    }

    // 매일 오후 4시 결제 완료에서 배송중으로 변경
    @Scheduled(cron = OrderConstants.STATE_CHANGE_TIME)
    public void scheduleChangeStateToShipped() {
        Set<String> set = redisService.getSetValues(OrderConstants.AWAIT_SHIP);
        for (String o : set) {
            orderService.updateOrderStateToShipped(Long.parseLong( o));
            redisService.addSetValue(OrderConstants.SHIPPED, o);
        }
        redisService.deleteValues(OrderConstants.AWAIT_SHIP);
    }

}
