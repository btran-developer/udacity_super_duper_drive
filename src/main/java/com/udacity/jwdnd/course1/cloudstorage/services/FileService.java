package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getUserFiles(String userName) {
        return fileMapper.getFilesByUserName(userName);
    }

    public File getFile(Integer fileId) {
        return fileMapper.getFileById(fileId);
    }

    public int storeNewFile(File file) {
        return fileMapper.insertFile(file);
    }

    public int removeFile(Integer fileId, Integer userId) { return fileMapper.deleteFile(fileId, userId); }

    public int removeAllUserFiles(Integer userId) { return fileMapper.deleteUserFiles(userId); }

    public boolean isFilenameUsed(String filename, String username) {
        File file = fileMapper.getUserFile(filename, username);

        return (file != null);
    }
}
