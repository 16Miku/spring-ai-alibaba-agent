package com.miku.springaialibabaagent.mapper;


import com.miku.springaialibabaagent.pojo.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 订单项数据访问接口
 */
@Mapper
public interface OrderItemMapper {

    /**
     * 批量插入订单项
     * 注意：使用注解实现批量插入通常不如使用Mapper XML配合<foreach>标签高效，
     * 这里的实现是将多条INSERT语句拼接在一起，可能受数据库连接字符串参数限制。
     * 更优的批量插入方式通常推荐使用XML。
     * @param orderItems 订单项列表
     * @return 影响的总行数
     */
    @Insert({
            "<script>", // 使用<script>标签支持动态SQL
            "INSERT INTO order_items (order_id, item_id, item_name, item_price, quantity) VALUES",
            "<foreach collection='orderItems' item='item' separator=','>", // 遍历orderItems列表
            "(#{item.orderId}, #{item.itemId}, #{item.itemName}, #{item.itemPrice}, #{item.quantity})", // 每个订单项的数据
            "</foreach>",
            "</script>"
    })
    int batchInsert(@Param("orderItems") List<OrderItem> orderItems); // 使用@Param指定集合参数名

    /**
     * 根据订单ID查询订单项列表
     * @param orderId 订单ID
     * @return 订单项列表
     */
    @Select("SELECT id, order_id, item_id, item_name, item_price, quantity, create_time, update_time FROM order_items WHERE order_id = #{orderId}")
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);
}
