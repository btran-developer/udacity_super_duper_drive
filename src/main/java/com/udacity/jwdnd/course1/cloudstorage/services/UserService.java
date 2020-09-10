package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private UserMapper userMapper;
    private HashService hashService;
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;

    public UserService(UserMapper userMapper, HashService hashService, FileService fileService, NoteService noteMapper, CredentialService credentialMapper) {
        this.userMapper = userMapper;
        this.hashService = hashService;
        this.fileService = fileService;
        this.noteService = noteMapper;
        this.credentialService = credentialMapper;
    }

    public boolean isUserNameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }

    public int createUser(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userMapper.insertUser(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }

    public int removeUserByUsername(String username) {
        User user = userMapper.getUser(username);
        fileService.removeAllUserFiles(user.getUserId());
        noteService.removeAllUserNotes(user.getUserId());
        credentialService.removeAllUserCredentials(user.getUserId());
        return userMapper.deleteUser(username);
    }

    public User getUserByUserName(String username) {
        return userMapper.getUser(username);
    }
}
