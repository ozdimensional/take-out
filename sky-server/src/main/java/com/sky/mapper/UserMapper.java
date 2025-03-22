package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /*
     * 根据openid查询用户信息
     */

    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User getUserByOpenid(String openid);

    /*
     * 插入用户信息
     */
    void insertUser(User user);

    User getById(Long userId);

    Integer countByMap(Map map);
}
