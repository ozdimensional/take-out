package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")// 每分钟执行一次
    public void ProcessTimeOutOrder() {
        log.info("定时处理超时订单" + LocalDateTime.now());

        LocalDateTime needTime = LocalDateTime.now().plusMinutes(-15);

        List<Orders> orders = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, needTime);

        if (!orders.isEmpty()) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("订单超时未支付，系统自动取消订单");
                orderMapper.update(order);
            }
        }
    }

    /*处理处于派送中的订单*/

    @Scheduled(cron = "0 0 1 * * ?")// 每天凌晨1点执行一次
    public void ProcessDeliveryOrder() {
        log.info("定时处理派送中的订单" + LocalDateTime.now());
        LocalDateTime needTime = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orders = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, needTime);
        if (!orders.isEmpty()) {
            for (Orders order : orders) {
                order.setStatus(Orders.COMPLETED);
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
                log.info("订单" + order.getId() + "已完成");
            }
        }
    }
}
