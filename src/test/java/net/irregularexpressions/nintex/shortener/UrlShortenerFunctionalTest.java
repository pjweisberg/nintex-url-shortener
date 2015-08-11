package net.irregularexpressions.nintex.shortener;

import org.apache.commons.lang.RandomStringUtils;
import org.bongiorno.ws.core.client.RelativeRestOperations;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UrlShortenerFunctionalTest {

    private RestTemplate restTemplate = new RelativeRestOperations("http://localhost:8080");

    @Test
    public void testRandomUrl() throws Exception {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add("targetUri", "http://www.google.com");

        URI result = restTemplate.postForLocation("/create", req);

        ClientHttpResponse response = restTemplate.execute(result, HttpMethod.HEAD, null, x -> x);
        assertEquals(301, response.getRawStatusCode());
        assertEquals(new URI("http://www.google.com"), response.getHeaders().getLocation());
    }

    @Test
    public void testSpecifiedCode() throws Exception {
        String randomCode = RandomStringUtils.randomAlphanumeric(5);

        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add("targetUri", "http://www.google.com");
        req.add("requestedShortCode", randomCode);

        URI result = restTemplate.postForLocation("/create", req);
        assertEquals(new URI("http://localhost:8080/" + randomCode), result);

        ClientHttpResponse response = restTemplate.execute(result, HttpMethod.HEAD, null, x -> x);
        assertEquals(301, response.getRawStatusCode());
        assertEquals(new URI("http://www.google.com"), response.getHeaders().getLocation());
    }


    @Test
    public void testInvalidUrl() throws Exception {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add("targetUri", "fizzBuzz?????");

        try {
            URI result = restTemplate.postForLocation("/create", req);
            fail();
        }catch (HttpClientErrorException e){
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void testInvalidCode() throws Exception {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add("targetUri", "http://www.google.com");
        req.add("requestedShortCode", "i/am/a/fish");

        try {
            URI result = restTemplate.postForLocation("/create", req);
            fail();
        }catch (HttpClientErrorException e){
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void testMissingUrl() throws Exception {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add("requestedShortCode", "whatever");


        try {
            URI result = restTemplate.postForLocation("/create", req);
            fail();
        }catch (HttpClientErrorException e){
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void testRequestUsedCode() throws Exception {

        String randomCode = RandomStringUtils.randomAlphanumeric(5);

        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add("targetUri", "http://www.google.com");
        req.add("requestedShortCode", randomCode);

        URI result = restTemplate.postForLocation("/create", req);
        assertEquals(new URI("http://localhost:8080/" + randomCode), result);

        req.put("targetUri", singletonList("http://www.something.else"));
        try {
            restTemplate.postForLocation("/create", req);
            fail();
        }catch (HttpClientErrorException e){
            assertEquals(HttpStatus.CONFLICT, e.getStatusCode());
        }

    }
}
