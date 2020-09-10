package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * " +
            "FROM notes")
    public List<Note> getNotes();

    @Select("SELECT * " +
            "FROM notes " +
            "WHERE noteid = #{noteId}")
    public Note getNoteById(Integer noteId);

    @Select("SELECT *" +
            "FROM notes n " +
            "JOIN users u " +
            "ON n.userid = u.userid " +
            "WHERE u.username = #{userName}")
    public List<Note> getNotesByUserName(String userName);

    @Insert("INSERT INTO notes " +
            "(notetitle, notedescription, userid) " +
            "VALUES " +
            "(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    public int insertNote(Note note);

    @Update("UPDATE notes " +
            "SET notetitle = #{noteTitle}, " +
            "notedescription = #{noteDescription} " +
            "WHERE noteid = #{noteId}")
    public int updateNote(Note note);

    @Delete("DELETE FROM notes " +
            "WHERE noteid = #{noteId} " +
            "AND userid = #{userId}")
    public int deleteNote(Integer noteId, Integer userId);

    @Delete("DELETE FROM notes " +
            "WHERE userid = #{userId}")
    public int deleteUserNotes(Integer userId);

}
