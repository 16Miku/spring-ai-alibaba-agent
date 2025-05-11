package com.miku.springaialibabaagent.mapper;

import com.miku.springaialibabaagent.pojo.Payment; // 假设你的Payment类在pojo包下
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

/**
 * 支付数据访问接口
 */
@Mapper
public interface PaymentMapper {

    /**
     * 插入新支付记录
     * @param payment 支付对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO payments (order_id, amount, status, payment_method, transaction_id) VALUES (#{orderId}, #{amount}, #{status}, #{paymentMethod}, #{transactionId})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 配置MyBatis将生成的自增主键设置回payment对象的id属性
    int insert(Payment payment);

    /**
     * 根据支付ID更新支付状态和交易ID
     * @param id 支付ID
     * @param status 新状态
     * @param transactionId 支付平台交易ID (支付成功时提供)
     * @return 影响的行数
     */
    @Update({
            "<script>",
            "UPDATE payments",
            "<set>",
            "status = #{status},", // 状态是必须更新的
            // 只有当 transactionId 不为 null 时才更新 transaction_id 字段
            "<if test='transactionId != null'>transaction_id = #{transactionId},</if>",
            // update_time 在SQL表中设置为 ON UPDATE CURRENT_TIMESTAMP，所以这里不需要手动更新
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    // 修正点1: 确保方法签名包含所有参数，与SQL中的#{...}对应
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("transactionId") String transactionId);

    /**
     * 根据订单ID查询支付记录
     * @param orderId 订单ID
     * @return 支付对象，如果不存在则返回null
     */
    @Select("SELECT id, order_id, amount, status, payment_method, transaction_id, create_time, update_time FROM payments WHERE order_id = #{orderId}")
    Payment selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据支付ID查询支付记录
     * 修正点2: 添加 selectById 方法，用于在 Service 层根据支付ID获取支付对象
     * @param id 支付ID
     * @return 支付对象，如果不存在则返回null
     */
    @Select("SELECT id, order_id, amount, status, payment_method, transaction_id, create_time, update_time FROM payments WHERE id = #{id}")
    Payment selectById(@Param("id") Long id);

    // 示例：根据交易ID查询支付记录（如果需要，例如处理支付回调时）
    // @Select("SELECT id, order_id, amount, status, payment_method, transaction_id, create_time, update_time FROM payments WHERE transaction_id = #{transactionId}")
    // Payment selectByTransactionId(@Param("transactionId") String transactionId);
}
