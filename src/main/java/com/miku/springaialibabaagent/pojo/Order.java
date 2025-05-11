package com.miku.springaialibabaagent.pojo;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List; // 订单可能包含多个订单项

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id; // 订单ID
    private Long userId; // 下单用户ID
    private BigDecimal totalAmount; // 订单总金额
    private String status; // 订单状态
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间

    // 关联的订单项列表，查询订单时可能需要一起加载
    private List<OrderItem> orderItems;
}

