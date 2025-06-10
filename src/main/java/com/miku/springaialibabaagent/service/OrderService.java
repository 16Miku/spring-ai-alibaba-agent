package com.miku.springaialibabaagent.service;



import com.miku.springaialibabaagent.pojo.Order;
import com.miku.springaialibabaagent.pojo.OrderItem;

import java.util.List;

public interface OrderService {

    /**
     * 创建新订单
     * @param userId 下单用户ID
     * @param items 订单项列表（包含商品ID和数量）
     * @return 创建成功的订单对象，如果失败则返回null
     */
    Order createOrder(Long userId, List<OrderItem> items);

    /**
     * 根据订单ID获取订单详情
     * @param orderId 订单ID
     * @return 订单对象（可能包含订单项列表）
     */
    Order getOrderById(Long orderId);

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @return 是否成功更新
     */
    boolean updateOrderStatus(Long orderId, String status);

    // 可以添加取消订单、查询用户订单列表等方法
    // boolean cancelOrder(Long orderId);


    /**
     * 获取用户历史订单
     * @param userId
     * @return
     */
    List<Order> getOrdersByUserId(Long userId);


}

