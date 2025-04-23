package org.flechaamarilla.client;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface ValidateClient {

    @POST
    @Path("/validate")
    @Consumes(MediaType.TEXT_PLAIN)  // Add this to specify XML content type
    @Produces(MediaType.APPLICATION_JSON)
    Response validateBill(String xmlContent);
}