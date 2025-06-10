package com.miku.springaialibabaagent.service.impl;


import com.miku.springaialibabaagent.mapper.OrderItemMapper;
import com.miku.springaialibabaagent.mapper.OrderMapper;
import com.miku.springaialibabaagent.pojo.Item;
import com.miku.springaialibabaagent.pojo.Order;
import com.miku.springaialibabaagent.pojo.OrderItem;
import com.miku.springaialibabaagent.service.ItemService;
import com.miku.springaialibabaagent.service.OrderService;
import com.miku.springaialibabaagent.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ItemService itemService; // 依赖商品服务
    private final UserService userService; // 依赖用户服务

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper, ItemService itemService, UserService userService) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.itemService = itemService;
        this.userService = userService;
    }

    @Override
    @Transactional // 创建订单是一个复杂的业务操作，需要事务保证原子性
    public Order createOrder(Long userId, List<OrderItem> items) {
        if (userId == null || items == null || items.isEmpty()) {
            log.warn("Invalid parameters for createOrder: userId={}, items={}", userId, items);
            return null;
        }

        // 1. 检查用户是否存在
        if (userService.getUserById(userId) == null) {
            log.warn("User with ID {} not found.", userId);
            // 实际应用中应抛出业务异常
            return null;
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> validOrderItems = new ArrayList<>();

        // 2. 验证商品信息，计算总金额，并检查库存
        for (OrderItem item : items) {
            if (item.getItemId() == null || item.getQuantity() <= 0) {
                log.warn("Invalid order item: itemId={}, quantity={}", item.getItemId(), item.getQuantity());
                // 实际应用中应抛出业务异常或跳过此项
                continue;
            }

            Item itemInfo = itemService.getItemById(item.getItemId());
            if (itemInfo == null) {
                log.warn("Item with ID {} not found.", item.getItemId());
                // 实际应用中应抛出业务异常
                throw new RuntimeException("Item not found: " + item.getItemId()); // 抛出异常触发事务回滚
            }

            if (itemInfo.getStock() < item.getQuantity()) {
                log.warn("Insufficient stock for item ID {}. Required: {}, Available: {}", item.getItemId(), item.getQuantity(), itemInfo.getStock());
                // 实际应用中应抛出业务异常
                throw new RuntimeException("Insufficient stock for item: " + itemInfo.getName()); // 抛出异常触发事务回滚
            }

            // 构建用于保存到数据库的OrderItem对象，记录下单时的价格和名称
            OrderItem validItem = new OrderItem();
            validItem.setItemId(item.getItemId());
            validItem.setItemName(itemInfo.getName()); // 记录下单时的商品名称
            validItem.setItemPrice(itemInfo.getPrice()); // 记录下单时的商品价格
            validItem.setQuantity(item.getQuantity());
            validOrderItems.add(validItem);

            // 计算此订单项的金额并累加到总金额
            totalAmount = totalAmount.add(itemInfo.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        if (validOrderItems.isEmpty()) {
            log.warn("No valid order items provided.");
            return null; // 没有有效的订单项，不创建订单
        }

        // 3. 创建订单主记录
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING"); // 初始状态为待支付
        // createTime和updateTime由数据库自动填充或在代码中设置

        int orderAffectedRows = orderMapper.insert(order);
        if (orderAffectedRows <= 0 || order.getId() == null) {
            log.error("Failed to insert order for user ID {}", userId);
            throw new RuntimeException("Failed to create order"); // 抛出异常触发事务回滚
        }
        log.info("Order created successfully with ID: {}", order.getId());

        // 4. 批量插入订单项
        for (OrderItem item : validOrderItems) {
            item.setOrderId(order.getId()); // 设置订单项所属的订单ID
        }
        int orderItemAffectedRows = orderItemMapper.batchInsert(validOrderItems);
        if (orderItemAffectedRows != validOrderItems.size()) {
            log.error("Failed to insert all order items for order ID {}. Expected: {}, Inserted: {}", order.getId(), validOrderItems.size(), orderItemAffectedRows);
            throw new RuntimeException("Failed to create order items"); // 抛出异常触发事务回滚
        }
        log.info("Order items inserted successfully for order ID: {}", order.getId());

        // 5. 减少商品库存
        for (OrderItem item : validOrderItems) {
            boolean decreased = itemService.decreaseStock(item.getItemId(), item.getQuantity());
            if (!decreased) {
                log.error("Failed to decrease stock for item ID {} during order creation for order ID {}", item.getItemId(), order.getId());
                throw new RuntimeException("Failed to decrease stock for item: " + item.getItemId()); // 抛出异常触发事务回滚
            }
        }
        log.info("Stock decreased successfully for items in order ID: {}", order.getId());

        // 6. 返回创建成功的订单对象
        order.setOrderItems(validOrderItems); // 将订单项设置回订单对象
        return order;
    }

    @Override
    public Order getOrderById(Long orderId) {
        if (orderId == null) {
            log.warn("Order ID is null when fetching order.");
            return null;
        }
        log.info("Fetching order with ID: {}", orderId);
        // 实际查询时，可能需要在Mapper XML中配置关联查询，将orderItems一起查出来
        Order order = orderMapper.selectById(orderId);
        if (order != null) {
            // 如果Mapper XML没有配置关联查询，这里可以单独查询订单项并设置进去
            List<OrderItem> orderItems = orderItemMapper.selectByOrderId(orderId);
            order.setOrderItems(orderItems);
        }
        return order;
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(Long orderId, String status) {
        if (orderId == null || status == null || status.trim().isEmpty()) {
            log.warn("Invalid parameters for updateOrderStatus: orderId={}, status={}", orderId, status);
            return false;
        }
        log.info("Updating status for order ID {} to {}", orderId, status);
        int affectedRows = orderMapper.updateStatus(orderId, status);
        if (affectedRows > 0) {
            log.info("Order status updated successfully for ID {}", orderId);
            return true;
        } else {
            log.warn("Failed to update status for order ID {}. Order might not exist.", orderId);
            return false;
        }
    }




    /**
     * 获取用户历史订单
     * @param userId
     * @return
     */
    // 实现 OrderService 接口中的 getOrdersByUserId 方法
    // 访问权限必须是 public，与接口定义一致
    @Override // 加上 @Override 注解是个好习惯，可以帮助编译器检查是否正确实现了接口方法
    public List<Order> getOrdersByUserId(Long userId) {

        // 检查用户ID是否为空
        if (userId == null) {
            log.warn("User ID is null when fetching orders by user ID.");
            return new ArrayList<>(); // 返回空列表或抛出异常，取决于业务需求
        }
        log.info("Fetching orders for user with ID: {}", userId);


        // 用用户id查询所有订单号
        List<Long> orderIds = orderMapper.selectOrderIdsByUserId(userId);

        // 创建订单列表
        List<Order> paidOrders  = new ArrayList<>();

        // 遍历订单号
        for( Long orderId : orderIds ) {

            // 根据订单号查询订单信息
            Order order = getOrderById(orderId);

            // 将已支付订单存入查询的订单列表
            if( order.getStatus().equals("PAID")  ) {

                paidOrders.add( order );
            }


        }


        return paidOrders ;



    }






}

