package net.irregularexpressions.nintex.shortener;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Random;

@Component
public class Base64ShortCodeGenerationStrategy implements ShortCodeGenerationStrategy {

    private Random r = new SecureRandom();

    @Value("${random.bytes:16}")
    private int randomBytes;

    public String generate(URI uri) {
        byte[] bytes = new byte[randomBytes];
        r.nextBytes(bytes);
        return Base64.encodeBase64URLSafeString(bytes);
    }
}
