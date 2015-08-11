package net.irregularexpressions.nintex.shortener.service;

import java.net.URI;
import java.util.UUID;

//@Component
public class UUIDShortCodeGenerationStrategy implements ShortCodeGenerationStrategy {
    public String generate(URI uri) {
        return UUID.randomUUID().toString();
    }
}
