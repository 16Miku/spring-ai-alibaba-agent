package com.miku.springaialibabaagent.mapper;




import com.miku.springaialibabaagent.pojo.User;
import org.apache.ibatis.annotations.*;

/**
 * 用户数据访问接口
 */
@Mapper // 标记这是一个MyBatis Mapper接口，Spring Boot会自动扫描并创建Bean
public interface UserMapper {

    /**
     * 根据用户ID查询用户信息
     * @param id 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    @Select("SELECT id, username, password, email, create_time, update_time FROM users WHERE id = #{id}")
    User selectById(@Param("id") Long id); // 使用@Param注解指定参数名，方便在SQL中引用

    /**
     * 插入新用户
     * @param user 用户对象
     * @return 影响的行数
     */
    @Insert("INSERT INTO users (username, password, email) VALUES (#{username}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id") // 配置MyBatis将生成的自增主键设置回user对象的id属性
    int insert(User user);

    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 影响的行数
     */
    @Update({
            "<script>", // 使用<script>标签支持动态SQL
            "UPDATE users",
            "<set>", // <set>标签用于构建SET子句，自动处理逗号
            "<if test='username != null'>username = #{username},</if>", // 如果username不为空，则更新username字段
            "<if test='password != null'>password = #{password},</if>", // 如果password不为空，则更新password字段
            "<if test='email != null'>email = #{email},</if>", // 如果email不为空，则更新email字段
            // update_time 在SQL表中设置为 ON UPDATE CURRENT_TIMESTAMP，所以这里不需要手动更新
            "</set>",
            "WHERE id = #{id}",
            "</script>"
    })
    int update(User user);

    // 示例：根据用户名查询用户（如果需要）
    // @Select("SELECT id, username, password, email, create_time, update_time FROM users WHERE username = #{username}")
    // User selectByUsername(@Param("username") String username);
}

