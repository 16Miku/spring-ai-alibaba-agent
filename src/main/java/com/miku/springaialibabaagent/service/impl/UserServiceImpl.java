package com.miku.springaialibabaagent.service.impl;


import com.miku.springaialibabaagent.mapper.UserMapper;
import com.miku.springaialibabaagent.pojo.User;
import com.miku.springaialibabaagent.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            log.warn("User ID is null when fetching user.");
            return null;
        }
        log.info("Fetching user with ID: {}", userId);
        return userMapper.selectById(userId);
    }

    @Override
    @Transactional
    public boolean createUser(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            log.warn("Invalid user data for creation.");
            return false;
        }
        log.info("Creating user: {}", user.getUsername());
        // 在实际应用中，这里需要对密码进行加密
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            int affectedRows = userMapper.insert(user);
            if (affectedRows > 0) {
                log.info("User created successfully with ID: {}", user.getId());
                return true;
            } else {
                log.warn("Failed to create user: {}", user.getUsername());
                return false;
            }
        } catch (Exception e) {
            // 捕获可能的唯一约束冲突异常（如用户名或邮箱已存在）
            log.error("Error creating user: {}", user.getUsername(), e);
            // 实际应用中需要根据异常类型进行更细致的处理和反馈
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        if (user == null || user.getId() == null) {
            log.warn("Invalid user data for update: ID is null.");
            return false;
        }
        log.info("Updating user with ID: {}", user.getId());
        // 如果密码字段不为空，可能需要先加密再更新
        // if (user.getPassword() != null) {
        //     user.setPassword(passwordEncoder.encode(user.getPassword()));
        // }
        int affectedRows = userMapper.update(user);
        if (affectedRows > 0) {
            log.info("User updated successfully with ID: {}", user.getId());
            return true;
        } else {
            log.warn("Failed to update user with ID {}. User might not exist.", user.getId());
            return false;
        }
    }
}

