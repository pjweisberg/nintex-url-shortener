package net.irregularexpressions.nintex.shortener;

import java.net.URI;

public interface ShortCodeGenerationStrategy {

    public String generate(URI uri);
}
