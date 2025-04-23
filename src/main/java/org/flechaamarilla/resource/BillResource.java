package org.flechaamarilla.resource;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.flechaamarilla.BillBuilderService;
import org.flechaamarilla.client.ValidateClient;
import org.flechaamarilla.dto.BillRequestDTO;
import org.flechaamarilla.model.BillXML;

import java.util.Map;

@Path("/factura")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BillResource {

    @Inject
    BillBuilderService builder;

    @Inject
    @RestClient
    ValidateClient validateClient;

    @POST
    public Response createBill(BillRequestDTO dto) {
        String xml = builder.generarXML(dto);

        try {
            // Attempt to validate the bill
            Response validationResponse = validateClient.validateBill(xml);

            // If we get a successful response, return the XML with validated status
            if (validationResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                BillXML billXML = new BillXML(xml, true);
                return Response.ok(billXML).build();
            } else {
                // Handle other successful responses but non-OK status codes
                return Response.status(validationResponse.getStatus())
                    .entity(validationResponse.readEntity(Map.class))
                    .build();
            }
        } catch (WebApplicationException e) {
            // REST client converts HTTP errors to WebApplicationException
            // Extract the error response from the exception
            Response errorResponse = e.getResponse();

            if (errorResponse.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
                // Handle validation errors from the service
                Map<String, Object> errorEntity = errorResponse.readEntity(Map.class);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errorEntity)
                        .build();
            } else {
                // Handle other errors (like 500)
                return Response.status(errorResponse.getStatus())
                        .entity(Map.of("error", "Validation service error: " + e.getMessage()))
                        .build();
            }
        } catch (Exception e) {
            // Handle unexpected errors
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Unexpected error: " + e.getMessage()))
                    .build();
        }
    }

}
