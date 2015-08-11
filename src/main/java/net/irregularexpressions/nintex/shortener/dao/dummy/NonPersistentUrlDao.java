package net.irregularexpressions.nintex.shortener.dao.dummy;

import net.irregularexpressions.nintex.shortener.CodeInUseException;
import net.irregularexpressions.nintex.shortener.service.ShortCodeGenerationStrategy;
import net.irregularexpressions.nintex.shortener.dao.UrlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class NonPersistentUrlDao implements UrlDao {

    private Map<String, URI> database = new HashMap<String, URI>();

    @Autowired
    private ShortCodeGenerationStrategy strategy;

    public synchronized URI get(String shortCode) {
        Objects.requireNonNull(shortCode, "Null short code");
        return database.get(shortCode);
    }

    public synchronized String createShortCode(URI uri) {
        Objects.requireNonNull(uri, "Null URI");
        String code = strategy.generate(uri);
        try {
            assignNewShortCode(uri, code);
        } catch (CodeInUseException e) {
            throw new RuntimeException("Short code generation strategy generated nonunique code");
        }
        return code;
    }

    public void assignNewShortCode(URI uri, String code) throws CodeInUseException {
        Objects.requireNonNull(uri, "Null URI");
        Objects.requireNonNull(code, "Null short code");
        if (database.containsKey(code)) {
            throw new CodeInUseException(code);
        }
        database.put(code, uri);
    }
}
