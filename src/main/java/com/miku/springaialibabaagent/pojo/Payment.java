package com.miku.springaialibabaagent.pojo;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Long id; // 支付ID
    private Long orderId; // 关联订单ID
    private BigDecimal amount; // 支付金额
    private String status; // 支付状态
    private String paymentMethod; // 支付方式
    private String transactionId; // 支付平台交易ID
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间
}

