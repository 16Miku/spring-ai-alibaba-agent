package com.miku.springaialibabaagent.service;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.miku.springaialibabaagent.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record getItemByIdResponse( Long id, String name, BigDecimal price, Integer stock, Date createTime, Date updateTime ){
    }

    @Bean
    @Description("根据商品ID获取商品信息")
    public Function<getItemByIdRequest, getItemByIdResponse> getItemById( ) {


        return getItemByIdRequest -> {

            log.info("Tool 'getItemById' called with request: {}",getItemByIdRequest);

            Item item = itemService.getItemById(getItemByIdRequest.itemId);

            getItemByIdResponse response = new getItemByIdResponse(
                    item.getId(), item.getName(), item.getPrice(),item.getStock(),item.getCreateTime(),item.getUpdateTime()
            );


            log.info("Tool 'getItemById' response: {}",response);

            return response;


        };

    }










    public record getOrderByIdRequest( Long id ){
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record getOrderByIdResponse( Order order ){
    }

    @Bean
    @Description("根据订单ID获取订单详情")
    public Function<getOrderByIdRequest, getOrderByIdResponse> getOrderById( ) {


        return getOrderByIdRequest -> {

            log.info("Tool 'getOrderById' called with request: {}",getOrderByIdRequest);

            Order order = orderService.getOrderById(getOrderByIdRequest.id);

            getOrderByIdResponse response = new getOrderByIdResponse( order );

            log.info("Tool 'getOrderById' response: {}",response);

            return response;


        };

    }





    public record createOrderRequest( Long userId, List<OrderItem> items ){
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record createOrderResponse( Order order ){
    }

    @Bean
    @Description("创建新订单")
    public Function<createOrderRequest, createOrderResponse> createOrder( ) {


        return createOrderRequest -> {

            log.info("Tool 'createOrder' called with request: {}",createOrderRequest);

            Order order = orderService.createOrder(createOrderRequest.userId,createOrderRequest.items);

            createOrderResponse response = new createOrderResponse( order );

            log.info("Tool 'createOrder' response: {}",response);

            return response;


        };

    }







    public record updateOrderStatusRequest( Long orderId, String status ){
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record updateOrderStatusResponse( boolean isUpdateSuccess ){
    }

    @Bean
    @Description("更新订单状态")
    public Function<updateOrderStatusRequest, updateOrderStatusResponse> updateOrderStatus( ) {


        return updateOrderStatusRequest -> {

            log.info("Tool 'updateOrderStatus' called with request: {}",updateOrderStatusRequest);

            boolean isUpdateSuccess = orderService.updateOrderStatus(updateOrderStatusRequest.orderId,updateOrderStatusRequest.status);

            updateOrderStatusResponse response = new updateOrderStatusResponse( isUpdateSuccess );

            log.info("Tool 'updateOrderStatus' response: {}",response);

            return response;


        };

    }









    public record getUserByIdRequest( Long userId ){
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record getUserByIdResponse( User user ){
    }

    @Bean
    @Description("根据用户ID获取用户信息")
    public Function<getUserByIdRequest, getUserByIdResponse> getUserById( ) {


        return getUserByIdRequest -> {

            log.info("Tool 'getUserById' called with request: {}",getUserByIdRequest);

            User user = userService.getUserById(getUserByIdRequest.userId);

            getUserByIdResponse response = new getUserByIdResponse( user );

            log.info("Tool 'getUserById' response: {}",response);

            return response;


        };

    }



















    public record createUserRequest( User user ){
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record createUserResponse( boolean isCreateSuccess ){
    }

    @Bean
    @Description("创建新用户")
    public Function<createUserRequest, createUserResponse> createUser( ) {


        return createUserRequest -> {

            log.info("Tool 'createUser' called with request: {}",createUserRequest);

            boolean isCreateSuccess = userService.createUser(createUserRequest.user);

            createUserResponse response = new createUserResponse( isCreateSuccess );

            log.info("Tool 'createUser' response: {}",response);

            return response;


        };

    }







    public record updateUserRequest( User user ){
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record updateUserResponse( boolean isUpdateSuccess ){
    }

    @Bean
    @Description("更新用户信息")
    public Function<updateUserRequest, updateUserResponse> updateUser( ) {


        return updateUserRequest -> {

            log.info("Tool 'updateUser' called with request: {}",updateUserRequest);

            boolean isUpdateSuccess = userService.updateUser(updateUserRequest.user);

            updateUserResponse response = new updateUserResponse(  isUpdateSuccess );

            log.info("Tool 'updateUser' response: {}",response);

            return response;


        };

    }












    public record createPaymentRequest( Long orderId, BigDecimal amount, String paymentMethod ){
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record createPaymentResponse( Payment payment ){
    }

    @Bean
    @Description("为订单创建支付记录")
    public Function<createPaymentRequest, createPaymentResponse> createPayment( ) {


        return createPaymentRequest -> {

            log.info("Tool 'createPayment' called with request: {}",createPaymentRequest);

            Payment payment = payService.createPayment(createPaymentRequest.orderId,createPaymentRequest.amount,createPaymentRequest.paymentMethod);

            createPaymentResponse response = new createPaymentResponse( payment );

            log.info("Tool 'createPayment' response: {}",response);

            return response;


        };

    }








    public record getPaymentByOrderIdRequest( Long orderId ){
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record getPaymentByOrderIdResponse( Payment payment ){
    }

    @Bean
    @Description("根据订单ID获取支付记录")
    public Function<getPaymentByOrderIdRequest, getPaymentByOrderIdResponse> getPaymentByOrderId( ) {


        return getPaymentByOrderIdRequest -> {

            log.info("Tool 'getPaymentByOrderId' called with request: {}",getPaymentByOrderIdRequest);

            Payment payment = payService.getPaymentByOrderId(getPaymentByOrderIdRequest.orderId);

            getPaymentByOrderIdResponse response = new getPaymentByOrderIdResponse( payment );

            log.info("Tool 'getPaymentByOrderId' response: {}",response);

            return response;


        };

    }














    public record updatePaymentStatusRequest( Long paymentId, String status, String transactionId ){
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record updatePaymentStatusResponse( boolean isUpdateSuccess ){
    }

    @Bean
    @Description("更新支付状态")
    public Function<updatePaymentStatusRequest, updatePaymentStatusResponse> updatePaymentStatus( ) {


        return updatePaymentStatusRequest -> {

            log.info("Tool 'updatePaymentStatus' called with request: {}",updatePaymentStatusRequest);

            boolean isUpdateSuccess = payService.updatePaymentStatus(updatePaymentStatusRequest.paymentId,updatePaymentStatusRequest.status,updatePaymentStatusRequest.transactionId);

            updatePaymentStatusResponse response = new updatePaymentStatusResponse( isUpdateSuccess );

            log.info("Tool 'updatePaymentStatus' response: {}",response);

            return response;


        };

    }








    // 定义获取用户历史订单请求的数据结构
    public record getUserPurchaseHistoryRequest( Long userId ){
    }

    // 定义获取用户历史订单响应的数据结构
    // @JsonInclude(JsonInclude.Include.NON_NULL)
    public record getUserPurchaseHistoryResponse( List<Order> orders ){
    }

    @Bean
    @Description("获取用户历史订单")
    public Function<getUserPurchaseHistoryRequest, getUserPurchaseHistoryResponse> getUserPurchaseHistory( ) {


        return getUserPurchaseHistoryRequest -> {

            log.info("Tool 'GetUserPurchaseHistory' called with request: {}",getUserPurchaseHistoryRequest);

            List<Order> orders = orderService.getOrdersByUserId(getUserPurchaseHistoryRequest.userId);

            getUserPurchaseHistoryResponse response = new getUserPurchaseHistoryResponse( orders );

            log.info("Tool 'getUserPurchaseHistory' response: {}",response);

            return response;


        };

    }







}
