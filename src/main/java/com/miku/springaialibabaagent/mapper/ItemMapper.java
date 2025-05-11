package com.miku.springaialibabaagent.mapper;


import com.miku.springaialibabaagent.pojo.Item;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;

/**
 * 商品数据访问接口
 */
@Mapper
public interface ItemMapper {

    /**
     * 根据商品ID查询商品信息
     * @param id 商品ID
     * @return 商品对象，如果不存在则返回null
     */
    @Select("SELECT id, name, price, stock, create_time, update_time FROM items WHERE id = #{id}")
    Item selectById(@Param("id") Long id);

    /**
     * 根据商品ID查询商品价格
     * @param id 商品ID
     * @return 商品价格
     */
    @Select("SELECT price FROM items WHERE id = #{id}")
    BigDecimal selectPriceById(@Param("id") Long id);

    /**
     * 减少商品库存
     * 注意：在高并发场景下，简单的UPDATE语句可能不足以保证库存准确性，
     * 需要结合业务逻辑（先查询库存是否足够）和数据库锁（如 SELECT ... FOR UPDATE）
     * 或乐观锁等机制。这里的SQL仅为基本示例。
     * @param id 商品ID
     * @param quantity 减少的库存数量（应为正数）
     * @return 影响的行数
     */
    @Update("UPDATE items SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}")
    // 添加 AND stock >= #{quantity} 可以在数据库层面防止库存变为负数，并返回0表示库存不足
    int decreaseStock(@Param("id") Long id, @Param("quantity") int quantity);

    // 示例：增加商品库存（如果需要）
    // @Update("UPDATE items SET stock = stock + #{quantity} WHERE id = #{id}")
    // int increaseStock(@Param("id") Long id, @Param("quantity") int quantity);

    // 示例：插入新商品（如果需要）
    // @Insert("INSERT INTO items (name, price, stock) VALUES (#{name}, #{price}, #{stock})")
    // @Options(useGeneratedKeys = true, keyProperty = "id")
    // int insert(Item item);

    // 示例：更新商品信息（如果需要）
    // @Update({
    //     "<script>",
    //     "UPDATE items",
    //     "<set>",
    //     "<if test='name != null'>name = #{name},</if>",
    //     "<if test='price != null'>price = #{price},</if>",
    //     "<if test='stock != null'>stock = #{stock},</if>",
    //     "</set>",
    //     "WHERE id = #{id}",
    //     "</script>"
    // })
    // int update(Item item);
}
