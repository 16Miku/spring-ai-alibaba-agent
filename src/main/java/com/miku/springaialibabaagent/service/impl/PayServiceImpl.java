package com.miku.springaialibabaagent.service.impl;

import com.miku.springaialibabaagent.mapper.PaymentMapper;
import com.miku.springaialibabaagent.pojo.Order; // 假设你的Order类在pojo包下
import com.miku.springaialibabaagent.pojo.Payment; // 假设你的Payment类在pojo包下
import com.miku.springaialibabaagent.service.OrderService;
import com.miku.springaialibabaagent.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PayServiceImpl implements PayService {

    private static final Logger log = LoggerFactory.getLogger(PayServiceImpl.class);

    private final PaymentMapper paymentMapper;
    private final OrderService orderService; // 依赖订单服务

    @Autowired
    public PayServiceImpl(PaymentMapper paymentMapper, OrderService orderService) {
        this.paymentMapper = paymentMapper;
        this.orderService = orderService;
    }

    @Override
    @Transactional // 创建支付记录需要事务
    public Payment createPayment(Long orderId, BigDecimal amount, String paymentMethod) {
        // 参数校验
        if (orderId == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 || paymentMethod == null || paymentMethod.trim().isEmpty()) {
            log.warn("Invalid parameters for createPayment: orderId={}, amount={}, paymentMethod={}", orderId, amount, paymentMethod);
            return null;
        }

        // 1. 检查订单是否存在且状态正确（例如，PENDING）
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            log.warn("Order with ID {} not found for payment creation.", orderId);
            // 实际应用中应抛出业务异常，这里为了示例返回null
            return null;
        }
        // 实际应用中还需要检查订单状态是否允许支付，以及支付金额是否与订单金额一致

        // 2. 检查是否已经存在支付记录（一个订单通常只有一个支付记录）
        Payment existingPayment = paymentMapper.selectByOrderId(orderId);
        if (existingPayment != null) {
            log.warn("Payment record already exists for order ID {}.", orderId);
            // 实际应用中应返回现有支付记录或抛出异常
            return existingPayment;
        }

        // 3. 创建支付记录
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setStatus("PENDING"); // 初始状态为待支付
        payment.setPaymentMethod(paymentMethod);
        // transactionId 在支付成功回调时更新，这里先不设置

        int affectedRows = paymentMapper.insert(payment);
        // 检查插入是否成功，并且MyBatis是否将生成的ID设置回了payment对象
        if (affectedRows > 0 && payment.getId() != null) {
            log.info("Payment record created successfully for order ID {}. Payment ID: {}", orderId, payment.getId());
            return payment;
        } else {
            log.error("Failed to create payment record for order ID {}", orderId);
            // 实际应用中应抛出业务异常
            throw new RuntimeException("Failed to create payment record"); // 抛出异常触发事务回滚
        }
    }

    @Override
    @Transactional // 更新支付状态需要事务
    public boolean updatePaymentStatus(Long paymentId, String status, String transactionId) {
        // 参数校验
        if (paymentId == null || status == null || status.trim().isEmpty()) {
            log.warn("Invalid parameters for updatePaymentStatus: paymentId={}, status={}", paymentId, status);
            return false;
        }
        log.info("Updating status for payment ID {} to {}", paymentId, status);

        // 实际应用中，这里通常是处理支付平台的回调
        // 需要根据 paymentId 或 transactionId 找到支付记录
        // 验证金额、状态等信息
        // 更新支付记录状态和 transactionId
        // 如果支付成功，还需要更新关联订单的状态为 PAID

        // 调用 Mapper 更新支付状态和交易ID
        // 修正点1: 传递 transactionId 参数
        int affectedRows = paymentMapper.updateStatus(paymentId, status, transactionId);

        if (affectedRows > 0) {
            log.info("Payment status updated successfully for ID {}", paymentId);
            // 如果状态是 SUCCESS，还需要调用 OrderService 更新订单状态
            if ("SUCCESS".equalsIgnoreCase(status)) {
                // 修正点2: 调用 PaymentMapper 中新增的 selectById 方法来获取 payment 对象，从而获取 orderId
                Payment updatedPayment = paymentMapper.selectById(paymentId);
                if (updatedPayment != null) {
                    log.info("Payment SUCCESS for order ID {}. Updating order status.", updatedPayment.getOrderId());
                    // 调用 OrderService 更新订单状态
                    boolean orderStatusUpdated = orderService.updateOrderStatus(updatedPayment.getOrderId(), "PAID");
                    if (!orderStatusUpdated) {
                        log.error("Failed to update order status to PAID for order ID {} after payment success.", updatedPayment.getOrderId());
                        // 实际应用中需要处理这种情况，可能需要人工介入或重试机制
                        // 这里不抛出异常，因为支付状态已经更新成功，只是订单状态更新失败
                    }
                } else {
                    log.error("Payment record not found after status update for ID {}", paymentId);
                    // 实际应用中需要处理这种情况，可能需要人工介入
                }
            }
            return true;
        } else {
            log.warn("Failed to update status for payment ID {}. Payment might not exist or status is already the same.", paymentId);
            return false;
        }
    }

    @Override
    public Payment getPaymentByOrderId(Long orderId) {
        if (orderId == null) {
            log.warn("Order ID is null when fetching payment.");
            return null;
        }
        log.info("Fetching payment for order ID: {}", orderId);
        return paymentMapper.selectByOrderId(orderId);
    }
}
