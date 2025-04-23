package org.flechaamarilla.client;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * REST client for the signer service
 * 
 * TODO: Change language convention to English in future refactorings
 */
@Path("/api/signer")
@RegisterRestClient(configKey = "signer-api")
public interface SignerClient {

    @POST
    @Path("/sign")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    String signXml(String xmlContent);
}