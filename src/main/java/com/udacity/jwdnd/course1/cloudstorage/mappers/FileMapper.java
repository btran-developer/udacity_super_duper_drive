package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * " +
            "FROM files")
    public List<File> getFiles();

    @Select("SELECT * " +
            "FROM files f " +
            "JOIN users u " +
            "ON f.userid = u.userid " +
            "WHERE u.username = #{username}")
    public List<File> getFilesByUserName(String username);

    @Select("SELECT * " +
            "FROM files f " +
            "JOIN users u " +
            "ON f.userid = u.userid " +
            "WHERE f.filename = #{filename} " +
            "AND u.username = #{username}")
    public File getUserFile(String filename, String username);

    @Select("SELECT * " +
            "FROM files " +
            "WHERE fileid = #{fileId}")
    public File getFileById(Integer fileId);

    @Insert("INSERT INTO files " +
            "(filename, contenttype, filesize, userid, filedata) " +
            "VALUES " +
            "(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    public int insertFile(File file);

    @Update("UPDATE files " +
            "SET filename = #{fileName}, " +
            "contenttype = #{contentType}, " +
            "filesize = #{fileSize}, " +
            "filedata = #{fileData} " +
            "WHERE fileid = #{fileId}")
    public int updateFile(File file);

    @Delete("DELETE FROM files " +
            "WHERE fileid = #{fileId} " +
            "AND userid = #{userId}")
    public int deleteFile(Integer fileId, Integer userId);

    @Delete("DELETE FROM files " +
            "WHERE userid = #{userId}")
    public int deleteUserFiles(Integer userId);
}
