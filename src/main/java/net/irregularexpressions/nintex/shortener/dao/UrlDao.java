package net.irregularexpressions.nintex.shortener.dao;

import net.irregularexpressions.nintex.shortener.CodeInUseException;

import java.net.URI;

public interface UrlDao {

    public URI get(String shortCode);

    public String createShortCode(URI uri);

    public void assignNewShortCode(URI uri, String code) throws CodeInUseException;

}
