package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * " +
            "FROM credentials")
    public List<Credential> getCredentials();

    @Select("SELECT * " +
            "FROM credentials " +
            "WHERE credentialid = #{credentialId}")
    public Credential getCredentialById(Integer credentialId);

    @Select("SELECT * " +
            "FROM credentials c " +
            "JOIN users u " +
            "ON c.userid = u.userid " +
            "WHERE u.username = #{username}")
    public List<Credential> getCredentialsByUserName(String username);

    @Insert("INSERT INTO credentials " +
            "(url, username, key, password, userid) " +
            "VALUES " +
            "(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    public int insertCredential(Credential credential);

    @Update("UPDATE credentials " +
            "SET url = #{url}, " +
            "username = #{username}, " +
            "key = #{key}, " +
            "password = #{password} " +
            "WHERE credentialid = #{credentialId}")
    public int updateCredential(Credential credential);

    @Delete("DELETE FROM credentials " +
            "WHERE credentialid = #{credentialId} " +
            "AND userid = #{userId}")
    public int deleteCredential(Integer credentialId, Integer userId);

    @Delete("DELETE FROM credentials " +
            "WHERE userid = #{userId}")
    public int deleteUserCredentials(Integer userId);
}
