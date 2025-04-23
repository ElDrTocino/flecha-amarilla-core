package org.flechaamarilla.messaging;

import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.Map;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class ErrorRecoveryService {

    @Incoming("facturas-error")
    public CompletionStage<Void> processFailedMessages(Message<byte[]> message) {
        // Extract error details from headers
        Map<String, Object> headers = message.getMetadata(OutgoingRabbitMQMetadata.class)
                .orElse(new OutgoingRabbitMQMetadata()).getHeaders();

        String errorMessage = (String) headers.get("error-message");

        // Log the error, store for later analysis, or attempt recovery
        System.err.println("Processing failed message: " + errorMessage);

        // Must acknowledge the message when finished processing
        return message.ack();
    }
}