package tech.magicbook.analytics.api.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public final class ApiKeyGenerator {
    
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    private ApiKeyGenerator(){

    }

    public static String generate(){
        
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);

        return "mb_" + HexFormat.of().formatHex(randomBytes);
    }

    public static String hash(String key){
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                key.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 algoritm is not avaliable", exception);
        }
    }

}
