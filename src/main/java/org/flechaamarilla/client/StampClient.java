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
 * REST client for the stamp service
 * 
 * TODO: Change language convention to English in future refactorings
 */
@Path("/api/stamp")
@RegisterRestClient(configKey = "stamp-api")
public interface StampClient {

    @POST
    @Path("/stamp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    XmlResponseDTO stampXml(XmlRequestDTO xmlRequest);
}