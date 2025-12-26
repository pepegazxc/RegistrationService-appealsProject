package main.service;

import org.apache.el.parser.AstLambdaParameters;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UserIdentifierService {

    private static final String PREFIX = "USR-";
    private static final String ALPHABET="ANCDEFGHJKLMPQRSTUVWXYZ23456789";
    private static final int ID_LENGTH = 6;

    private static final SecureRandom RANDOM = new SecureRandom();

    public String generate(){
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < ID_LENGTH; i++){
            int index = RANDOM.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }
        return sb.toString();
    }
}
