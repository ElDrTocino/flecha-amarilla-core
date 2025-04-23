package org.flechaamarilla.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.flechaamarilla.client.SignerClient;
import org.flechaamarilla.client.StampClient;
import org.flechaamarilla.client.ValidatorClient;
import org.flechaamarilla.dto.BillRequestDTO;
import org.flechaamarilla.dto.BillResponseDTO;
import org.flechaamarilla.dto.XmlRequestDTO;
import org.flechaamarilla.dto.XmlResponseDTO;
import org.jboss.logging.Logger;

/**
 * Service to orchestrate the bill creation process through REST calls
 * 
 * TODO: Change language convention to English in future refactorings
 */
@ApplicationScoped
public class BillService {

    private static final Logger LOG = Logger.getLogger(BillService.class);

    @Inject
    BillBuilderService billBuilderService;

    @Inject
    @RestClient
    ValidatorClient validatorClient;

    @Inject
    @RestClient
    SignerClient signerClient;

    @Inject
    @RestClient
    StampClient stampClient;

    /**
     * Creates a complete bill by orchestrating calls to validator, signer and stamp services
     * 
     * @param billRequest The bill request data
     * @return The complete bill with signed and stamped XML
     */
    public BillResponseDTO createBill(BillRequestDTO billRequest) {
        LOG.info("Starting bill creation process for: " + billRequest.getFolio());

        try {
            // Step 1: Generate XML from request data
            String generatedXml = billBuilderService.generarXML(billRequest);
            LOG.info("XML generated successfully");
            
            // Step 2: Validate XML with validator service
            XmlRequestDTO validationRequest = new XmlRequestDTO(generatedXml);
            XmlResponseDTO validationResponse = validatorClient.validateXml(validationRequest);
            LOG.info("XML validated successfully: " + validationResponse.isValid());
            
            if (!validationResponse.isValid()) {
                throw new RuntimeException("XML validation failed: " + 
                        String.join(", ", validationResponse.getErrors()));
            }
            
            // Step 3: Sign XML with signer service - USING TEXT PLAIN APPROACH
            String signedXml = signerClient.signXml(generatedXml);
            
            // Verificar respuesta del servicio de firma
            if (signedXml == null || signedXml.trim().isEmpty() || signedXml.startsWith("ERROR:")) {
                throw new RuntimeException("XML signing failed: " + 
                        (signedXml != null ? signedXml : "No response from signer service"));
            }
            
            LOG.info("XML signed successfully");
            
            // Step 4: Stamp XML with stamp service
            XmlRequestDTO stampRequest = new XmlRequestDTO(signedXml);
            XmlResponseDTO stampResponse = stampClient.stampXml(stampRequest);
            String stampedXml = stampResponse.getXmlContent();
            LOG.info("XML stamped successfully");
            
            // Return the result
            return BillResponseDTO.builder()
                    .uuid(stampResponse.getUuid())
                    .xml(stampedXml)
                    .folio(billRequest.getFolio())
                    .serie(billRequest.getSerie())
                    .success(true)
                    .build();
                    
        } catch (Exception e) {
            LOG.error("Error in bill creation process", e);
            throw new RuntimeException("Error during bill creation: " + e.getMessage(), e);
        }
    }
}