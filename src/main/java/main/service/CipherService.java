package main.service;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

@Service
public class CipherService {

    private final StringEncryptor encryptor;

    public CipherService(StringEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    public String encrypt(String str){
        return encryptor.encrypt(str);
    }

    public String decrypt(String str){
        return encryptor.decrypt(str);
    }
}
