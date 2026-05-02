package main.service.infrastructure;

import lombok.extern.slf4j.Slf4j;
import main.exception.cryptography.DecryptException;
import main.exception.cryptography.EncryptException;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CipherService {

    private final StringEncryptor encryptor;

    public CipherService(StringEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    public String encrypt(String str){
        try {
            return encryptor.encrypt(str);
        }catch (Exception e){
            log.error("An error occurred while encrypting", e);
            throw new EncryptException();
        }
    }

    public String decrypt(String str){
        try {
            return encryptor.decrypt(str);
        }catch (Exception e){
            log.error("An error occurred while decrypting", e);
            throw new DecryptException();
        }
    }
}
