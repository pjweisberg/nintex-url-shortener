package net.irregularexpressions.nintex.shortener.ws.controller;

import net.irregularexpressions.nintex.shortener.CodeInUseException;
import net.irregularexpressions.nintex.shortener.service.UrlService;
import org.apache.cxf.jaxrs.ext.DefaultMethod;
import org.bongiorno.ws.core.exceptions.ConflictException;
import org.bongiorno.ws.core.exceptions.ResourceNotFoundException;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;


@Service
@Path("/")
@Validated
public class UrlController {

    @Autowired
    private UrlService urlService;

    @Path("/{code}")
    @DefaultMethod
    public Response forward(@PathParam("code") String shortCode) {
        URI url = urlService.get(shortCode);
        if (url == null) {
            throw new ResourceNotFoundException("No mapping for '%s'", shortCode);
        } else {
            return Response.status(Response.Status.MOVED_PERMANENTLY).location(url).build();
        }
    }


    @Path("/lookup/{code}")
    @GET
    @Produces("text/plain")
    public String lookup(@PathParam("code") String shortCode){
        URI url = urlService.get(shortCode);
        if (url == null) {
            throw new ResourceNotFoundException("No mapping for '%s'", shortCode);
        } else {
            return url.toString();
        }
    }

    @Path("/create")
    @POST
    public Response createNew(@Context HttpServletRequest hsr,

                              @URL(message = "${validatedValue} is not a valid URL") @FormParam("targetUri")
                              String targetUrl,

                              @Pattern(regexp = "[A-Za-z_-]*", message = "${validatedValue} does not match \"{regexp}\"")
                              @FormParam("requestedShortCode")
                              String code) throws URISyntaxException {
        try {
            code = urlService.put(code, new URI(targetUrl));
        } catch (CodeInUseException e) {
            throw new ConflictException(e, "Short URL code '%s' is already in use", code);
        }
        StringBuffer requestURL = hsr.getRequestURL();
        requestURL.setLength(requestURL.length() - 6);


        URI location = new URI(requestURL + code);
        return Response.created(location).entity(location.toString()).build();
    }


}