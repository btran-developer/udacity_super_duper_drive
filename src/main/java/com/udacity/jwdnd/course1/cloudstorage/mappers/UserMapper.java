package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * " +
            "FROM users " +
            "WHERE username = #{username}")
    public User getUser(String username);

    @Insert("INSERT INTO users " +
            "(username, salt, password, firstname, lastname) " +
            "VALUES " +
            "(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    public int insertUser(User user);

    @Delete("DELETE FROM users " +
            "WHERE username = #{username}")
    public int deleteUser(String username);
}
