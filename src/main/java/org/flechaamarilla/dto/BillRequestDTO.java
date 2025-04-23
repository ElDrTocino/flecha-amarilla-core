package org.flechaamarilla.dto;

import lombok.Data;
import mx.gob.sat.sitio_internet.cfd.catalogos.CMetodoPago;
import mx.gob.sat.sitio_internet.cfd.catalogos.CMoneda;
import mx.gob.sat.sitio_internet.cfd.catalogos.CUsoCFDI;

import java.util.List;

@Data
public class BillRequestDTO {
    private String rfcEmisor;
    private String nombreEmisor;
    private String rfcReceptor;
    private String nombreReceptor;
    private CUsoCFDI usoCfdi;
    private String formaPago;
    private CMetodoPago metodoPago;
    private CMoneda moneda;
    private String serie;
    private String folio;
    private List<ConceptoDTO> conceptos;

    @Data
    public static class ConceptoDTO {
        private String claveProdServ;
        private String descripcion;
        private Double cantidad;
        private String unidad;
        private Double valorUnitario;
        private Double importe;
    }
}
