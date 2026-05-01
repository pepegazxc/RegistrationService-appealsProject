package cipher;

import main.service.infrastructure.CipherService;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CipherTest {

    @Mock private StringEncryptor encryptor;

    @InjectMocks
    private CipherService cipherService;

    @Test
    public void testCipherEncrypt_success(){
        String input = "input";
        String encypted = "output";

        when(encryptor.encrypt(input)).thenReturn(encypted);

        String result = cipherService.encrypt(input);

        assertEquals(encypted, result);
        verify(encryptor).encrypt(input);

    }

    @Test
    public void testCipherDecrypt_success(){
        String str = "encryptedString";
        String decrypted = "decrypted";

        when(encryptor.decrypt(str)).thenReturn(decrypted);

        String result = cipherService.decrypt(str);

        assertEquals(decrypted, result);
        verify(encryptor).decrypt(str);
    }

}
