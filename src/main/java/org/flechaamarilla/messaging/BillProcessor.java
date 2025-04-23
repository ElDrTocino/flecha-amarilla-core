package org.flechaamarilla.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.flechaamarilla.BillBuilderService;
import org.flechaamarilla.dto.BillRequestDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
@Slf4j
public class BillProcessor {

    @Inject
    BillBuilderService builder;

    @Incoming("facturas-in")
    @Outgoing("facturas-procesadas")
    public CompletionStage<Message<String>> consume(Message<byte[]> message) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                BillRequestDTO billRequestDTO = mapper.readValue(message.getPayload(), BillRequestDTO.class);

                // Generate XML and process
                String xml = builder.generarXML(billRequestDTO);

                log.info("Procesada factura para: {} y enviada a cola", billRequestDTO.getNombreReceptor());

                // Return message with acknowledgment
                return Message.of(xml, () -> message.ack());

            } catch (Exception e) {
                log.error("Error processing message: {}", e.getMessage(), e);
                message.nack(e);
                return null;
            }
        });
    }
}