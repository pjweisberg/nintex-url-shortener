package net.irregularexpressions.nintex.shortener.service;

import java.net.URI;

public interface ShortCodeGenerationStrategy {

    public String generate(URI uri);
}
