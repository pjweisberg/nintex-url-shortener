package net.irregularexpressions.nintex.shortener.service;

import net.irregularexpressions.nintex.shortener.CodeInUseException;
import net.irregularexpressions.nintex.shortener.dao.UrlDao;
import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Arrays;

@Component
public class UrlService {

    @Autowired
    private UrlDao dao;

    @Value("${forbidden}")
    private String[] forbiddenCodes;

    public URI get(String shortCode) {
        return dao.get(shortCode);
    }

    public String put(URI uri) {
        return dao.createShortCode(uri);
    }

    public String put(String requestedShortCode, URI targetUri) throws CodeInUseException {
        String result;
        if (StringUtils.isEmpty(requestedShortCode)) {
            result = dao.createShortCode(targetUri);
        }else {
            if(Arrays.asList(forbiddenCodes).contains(requestedShortCode)){
                throw new IllegalArgumentException("You may not.");
            }
            dao.assignNewShortCode(targetUri, requestedShortCode);
            result = requestedShortCode;
        }
        return result;
    }
}
