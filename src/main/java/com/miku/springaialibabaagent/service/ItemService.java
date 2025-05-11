package com.miku.springaialibabaagent.service;



import com.miku.springaialibabaagent.pojo.Item;

import java.math.BigDecimal;

public interface ItemService {

    /**
     * 根据商品ID获取商品信息
     * @param itemId 商品ID
     * @return 商品对象
     */
    Item getItemById(Long itemId);

    /**
     * 根据商品ID获取商品价格
     * @param itemId 商品ID
     * @return 商品价格
     */
    BigDecimal getItemPrice(Long itemId);

    /**
     * 减少商品库存
     * @param itemId 商品ID
     * @param quantity 减少的数量
     * @return 是否成功减少库存
     */
    boolean decreaseStock(Long itemId, int quantity);

    // 可以添加增加库存、创建商品、更新商品等方法
    // boolean increaseStock(Long itemId, int quantity);
    // boolean createItem(Item item);
    // boolean updateItem(Item item);
}
