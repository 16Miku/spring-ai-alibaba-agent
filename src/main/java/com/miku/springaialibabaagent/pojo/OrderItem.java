package com.miku.springaialibabaagent.pojo;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private Long id; // 订单项ID
    private Long orderId; // 所属订单ID
    private Long itemId; // 商品ID
    private String itemName; // 商品名称（下单时）
    private BigDecimal itemPrice; // 商品价格（下单时）
    private Integer quantity; // 购买数量
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间
}

