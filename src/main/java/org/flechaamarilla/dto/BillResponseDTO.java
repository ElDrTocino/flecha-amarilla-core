package org.flechaamarilla.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for bill creation response
 * 
 * TODO: Change language convention to English in future refactorings
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponseDTO {
    private String uuid;
    private String xml;
    private String folio;
    private String serie;
    private boolean success;
    private String errorMessage;
}