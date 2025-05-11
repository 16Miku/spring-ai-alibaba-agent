package com.miku.springaialibabaagent.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// 使用Lombok注解自动生成getter, setter, toString, equals, hashCode, 无参构造函数, 全参构造函数
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id; // 用户ID
    private String username; // 用户名
    private String password; // 密码
    private String email; // 邮箱
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间
}