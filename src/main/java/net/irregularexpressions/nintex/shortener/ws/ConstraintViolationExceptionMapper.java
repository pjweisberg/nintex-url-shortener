package net.irregularexpressions.nintex.shortener.ws;

import org.bongiorno.ws.core.server.exceptions.mapping.AbstractExceptionMapper;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

public class ConstraintViolationExceptionMapper extends AbstractExceptionMapper<ConstraintViolationException> {
    @Override
    protected HttpStatus getHttpStatus(ConstraintViolationException exception) {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    protected Class<ConstraintViolationException> getSupportedException() {
        return ConstraintViolationException.class;
    }

    @Override
    protected Map<String, String> getErrorDetails(ConstraintViolationException exception) {
        Map<String, String> retVal = new HashMap<String, String>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            retVal.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return retVal;
    }
}
