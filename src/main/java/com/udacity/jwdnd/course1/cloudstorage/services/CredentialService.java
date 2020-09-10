package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    @Autowired
    private CredentialMapper credentialMapper;
    @Autowired
    private EncryptionService encryptionService;

    public List<Credential> getUserCredentials(String userName) {
        return credentialMapper.getCredentialsByUserName(userName);
    }

    public int storeNewCredential(Credential credential) {
        String encodedKey = encryptionService.generateEncodedKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);

        return credentialMapper.insertCredential(credential);
    }

    public int updateCredential(Credential credential) {
        String encryptedPassword;

        if(credential.getKey() == null) {
            String encodedKey = encryptionService.generateEncodedKey();
            encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

            credential.setKey(encodedKey);
        } else {
            encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        }

        credential.setPassword(encryptedPassword);

        return credentialMapper.updateCredential(credential);
    }

    public int removeCredential(Integer credentialId, Integer userId) { return credentialMapper.deleteCredential(credentialId, userId); }

    public int removeAllUserCredentials(Integer userId) { return credentialMapper.deleteUserCredentials(userId); }
}
