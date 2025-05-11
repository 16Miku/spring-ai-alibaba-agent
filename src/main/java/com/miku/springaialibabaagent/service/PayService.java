package com.miku.springaialibabaagent.service;



import com.miku.springaialibabaagent.pojo.Payment;

import java.math.BigDecimal;

public interface PayService {

    /**
     * 为订单创建支付记录
     * @param orderId 订单ID
     * @param amount 支付金额
     * @param paymentMethod 支付方式
     * @return 创建成功的支付对象，如果失败则返回null
     */
    Payment createPayment(Long orderId, BigDecimal amount, String paymentMethod);

    /**
     * 更新支付状态
     * @param paymentId 支付ID
     * @param status 新状态
     * @param transactionId 支付平台交易ID (支付成功时提供)
     * @return 是否成功更新
     */
    boolean updatePaymentStatus(Long paymentId, String status, String transactionId);

    /**
     * 根据订单ID获取支付记录
     * @param orderId 订单ID
     * @return 支付对象
     */
    Payment getPaymentByOrderId(Long orderId);

    // 可以添加处理支付回调、退款等方法
    // boolean handlePaymentCallback(Map<String, String> callbackData);
    // boolean refundPayment(Long paymentId, BigDecimal refundAmount);
}

