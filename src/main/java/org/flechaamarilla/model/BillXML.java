package org.flechaamarilla.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillXML {
    private String xml;
    private boolean validated;
}
