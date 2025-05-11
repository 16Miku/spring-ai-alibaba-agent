package com.miku.springaialibabaagent.mapper;


import com.miku.springaialibabaagent.pojo.Order;
import org.apache.ibatis.annotations.*;

/**
 * 订单数据访问接口
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入新订单
     * @param order 订单对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO orders (user_id, total_amount, status) VALUES (#{userId}, #{totalAmount}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 将生成的订单ID设置回order对象的id属性
    int insert(Order order);

    /**
     * 根据订单ID查询订单信息
     * 注意：此方法只查询订单主表信息，不包含订单项。
     * 如果需要同时查询订单项，可以在Mapper XML中配置resultMap进行关联查询，
     * 或者在Service层单独调用OrderItemMapper查询。
     * @param id 订单ID
     * @return 订单对象，如果不存在则返回null
     */
    @Select("SELECT id, user_id, total_amount, status, create_time, update_time FROM orders WHERE id = #{id}")
    Order selectById(@Param("id") Long id);

    /**
     * 更新订单状态
     * @param id 订单ID
     * @param status 新状态
     * @return 影响的行数
     */
    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    // 示例：根据用户ID查询订单列表（如果需要）
    // @Select("SELECT id, user_id, total_amount, status, create_time, update_time FROM orders WHERE user_id = #{userId}")
    // List<Order> selectByUserId(@Param("userId") Long userId);
}
