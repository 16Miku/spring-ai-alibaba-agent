package com.miku.springaialibabaagent.service;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.miku.springaialibabaagent.pojo.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Function;

@Configuration
@Slf4j
public class CustomTools {


    @Autowired
    private ItemService itemService;


    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @Autowired
    private UserService userService;



    public record getItemByIdRequest( Long itemId ){
    }



    @Bean
    @Description("根据商品ID获取商品信息")
    public Function<getItemByIdRequest, Item> getItemById( ) {


        return getItemByIdRequest -> {

//            log.info("Tool 'getItemById' called with request: {}",getItemByIdRequest);

            Item item = itemService.getItemById(getItemByIdRequest.itemId);

//            log.info("Tool 'getItemById' response: {}",item);

            return item;


        };

    }





    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record getItemByIdResponse( Long id, String name, BigDecimal price, Integer stock, Date createTime, Date updateTime ){
    }










}
