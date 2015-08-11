package net.irregularexpressions.nintex.shortener.service;

import net.irregularexpressions.nintex.shortener.CodeInUseException;
import net.irregularexpressions.nintex.shortener.dao.UrlDao;
import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class UrlService {

    @Autowired
    private UrlDao dao;

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
            dao.assignNewShortCode(targetUri, requestedShortCode);
            result = requestedShortCode;
        }
        return result;
    }
}
