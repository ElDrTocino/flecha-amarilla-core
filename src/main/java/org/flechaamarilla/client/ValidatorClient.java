package org.flechaamarilla.client;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.flechaamarilla.dto.XmlRequestDTO;
import org.flechaamarilla.dto.XmlResponseDTO;

/**
 * REST client for the validator service
 * 
 * TODO: Change language convention to English in future refactorings
 */
@Path("/api/validator")
@RegisterRestClient(configKey = "validator-api")
public interface ValidatorClient {

    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    XmlResponseDTO validateXml(XmlRequestDTO xmlRequest);
}