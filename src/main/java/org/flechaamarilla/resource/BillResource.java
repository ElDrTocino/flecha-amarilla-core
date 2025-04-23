package org.flechaamarilla.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.flechaamarilla.dto.BillRequestDTO;
import org.flechaamarilla.dto.BillResponseDTO;
import org.flechaamarilla.service.BillService;
import org.jboss.logging.Logger;

/**
 * REST endpoint for bill generation and processing
 * 
 * TODO: Change language convention to English in future refactorings
 */
@Path("/api/bills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BillResource {
    
    private static final Logger LOG = Logger.getLogger(BillResource.class);
    
    @Inject
    BillService billService;
    
    /**
     * Creates a complete bill by orchestrating the entire process:
     * 1. Generate XML
     * 2. Validate XML
     * 3. Sign XML
     * 4. Stamp XML
     */
    @POST
    @Path("/create")
    public BillResponseDTO createBill(BillRequestDTO request) {
        LOG.info("Received bill creation request for: " + request.getFolio());
        
        try {
            return billService.createBill(request);
            
        } catch (Exception e) {
            LOG.error("Error during bill creation", e);
            
            return BillResponseDTO.builder()
                    .success(false)
                    .folio(request.getFolio())
                    .serie(request.getSerie())
                    .errorMessage("Error during bill creation: " + e.getMessage())
                    .build();
        }
    }
}