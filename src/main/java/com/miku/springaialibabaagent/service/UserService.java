package com.miku.springaialibabaagent.service;


import com.miku.springaialibabaagent.pojo.User;

public interface UserService {

    /**
     * 根据用户ID获取用户信息
     * @param userId 用户ID
     * @return 用户对象
     */
    User getUserById(Long userId);

    /**
     * 创建新用户
     * @param user 用户对象
     * @return 是否成功创建
     */
    boolean createUser(User user);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 是否成功更新
     */
    boolean updateUser(User user);

    // 可以添加根据用户名查询、用户登录验证等方法
    // User getUserByUsername(String username);
    // boolean validateUser(String username, String password);
}

