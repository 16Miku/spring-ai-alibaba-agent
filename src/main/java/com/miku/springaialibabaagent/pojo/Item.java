package com.miku.springaialibabaagent.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id; // 商品ID
    private String name; // 商品名称
    private BigDecimal price; // 商品价格
    private Integer stock; // 库存数量
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间
}