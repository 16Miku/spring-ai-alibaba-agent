package com.miku.springaialibabaagent.service.impl;


import com.miku.springaialibabaagent.mapper.ItemMapper;
import com.miku.springaialibabaagent.pojo.Item;
import com.miku.springaialibabaagent.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service // 标记这是一个Spring Service组件
public class ItemServiceImpl implements ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemMapper itemMapper;

    // 推荐使用构造函数注入依赖
    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public Item getItemById(Long itemId) {
        log.info("Fetching item with ID: {}", itemId);
        return itemMapper.selectById(itemId);
    }

    @Override
    public BigDecimal getItemPrice(Long itemId) {
        log.info("Fetching price for item with ID: {}", itemId);
        return itemMapper.selectPriceById(itemId);
    }

    @Override
    @Transactional // 标记此方法需要事务支持，确保库存更新的原子性
    public boolean decreaseStock(Long itemId, int quantity) {
        if (itemId == null || quantity <= 0) {
            log.warn("Invalid parameters for decreaseStock: itemId={}, quantity={}", itemId, quantity);
            return false;
        }
        log.info("Decreasing stock for item ID {} by {}", itemId, quantity);
        // 在实际应用中，这里需要先检查库存是否足够，可以使用 SELECT ... FOR UPDATE 锁定行
        // 为了简化，这里直接调用更新，如果数据库字段设置为无符号或有检查约束，可以防止负库存
        int affectedRows = itemMapper.decreaseStock(itemId, quantity);
        if (affectedRows > 0) {
            log.info("Successfully decreased stock for item ID {}", itemId);
            return true;
        } else {
            // affectedRows == 0 可能表示商品不存在，或者库存不足（取决于SQL实现）
            log.warn("Failed to decrease stock for item ID {}. Affected rows: {}", itemId, affectedRows);
            // 实际应用中需要更精细的错误处理，例如抛出库存不足异常
            return false;
        }
    }
}

