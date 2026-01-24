package main.security;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.NoIvGenerator;
import org.jasypt.salt.ByteArrayFixedSaltGenerator;
import org.jasypt.salt.FixedSaltGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class CipherConfig {

    @Value("${Jasypt.encryptor.password}")
    private String password;

    @Value("${Jasypt.encryptor.algorithm}")
    private String algorithm;

    @Value("${Jasypt.encryptor.gcm-secret-key-salt}")
    private String salt;

    @Bean
    public StringEncryptor encryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(password);
        config.setAlgorithm(algorithm);
        config.setPoolSize("1");
        config.setKeyObtentionIterations("1000");
        config.setProviderName("SunJCE");
        config.setSaltGenerator(
                new ByteArrayFixedSaltGenerator(salt.getBytes(StandardCharsets.UTF_8))
        );
        config.setStringOutputType("base64");

        encryptor.setConfig(config);
        return encryptor;
    }
}
