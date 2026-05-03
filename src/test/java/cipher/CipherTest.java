package cipher;

import main.exception.cryptography.DecryptException;
import main.exception.cryptography.EncryptException;
import main.service.infrastructure.CipherService;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CipherTest {

    @Mock private StringEncryptor encryptor;

    @InjectMocks
    private CipherService cipherService;

    @Test
    public void encrypt_success_shouldEncryptString(){
        String input = "input";
        String encypted = "output";

        when(encryptor.encrypt(input)).thenReturn(encypted);

        String result = cipherService.encrypt(input);

        assertEquals(encypted, result);
        verify(encryptor).encrypt(input);

    }

    @Test
    public void decrypt_success_shouldDecryptString(){
        String str = "encryptedString";
        String decrypted = "decrypted";

        when(encryptor.decrypt(str)).thenReturn(decrypted);

        String result = cipherService.decrypt(str);

        assertEquals(decrypted, result);
        verify(encryptor).decrypt(str);
    }

    @Test
    public void encrypt_failed_shouldReturnEncryptException(){
        when(encryptor.encrypt(anyString())).thenThrow(new EncryptException());

        assertThrows(EncryptException.class, () -> cipherService.encrypt("str"));
    }

    @Test
    public void decrypt_failed_shouldReturnDecryptException(){
        when(encryptor.decrypt(anyString())).thenThrow(new DecryptException());

        assertThrows(DecryptException.class, () -> cipherService.decrypt("str"));
    }

}
