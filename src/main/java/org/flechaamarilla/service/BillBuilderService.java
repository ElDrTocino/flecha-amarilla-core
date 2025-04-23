package org.flechaamarilla.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import mx.gob.sat.cfd._4.Comprobante;
import mx.gob.sat.cfd._4.Comprobante.Conceptos;
import mx.gob.sat.cfd._4.Comprobante.Conceptos.Concepto;
import mx.gob.sat.cfd._4.Comprobante.Emisor;
import mx.gob.sat.cfd._4.Comprobante.Receptor;
import mx.gob.sat.sitio_internet.cfd.catalogos.CTipoDeComprobante;
import org.flechaamarilla.dto.BillRequestDTO;

import java.io.StringWriter;
import java.math.BigDecimal;

import static org.flechaamarilla.util.DateTimeConverter.toXMLGregorianCalendar;

@ApplicationScoped
public class BillBuilderService {

    public String generarXML(BillRequestDTO dto) {

        try {
            Comprobante comprobante = new Comprobante();
            comprobante.setVersion("4.0");
            comprobante.setFecha(toXMLGregorianCalendar());
            comprobante.setSerie(dto.getSerie());
            comprobante.setFolio(dto.getFolio());
            comprobante.setFormaPago(dto.getFormaPago());
            comprobante.setMetodoPago(dto.getMetodoPago());
            comprobante.setMoneda(dto.getMoneda());
            comprobante.setLugarExpedicion("06000"); // CDMX, ejemplo

            //mock values to pass structure validation
            comprobante.setSello("SELLOFAKE1234567890...");
            comprobante.setCertificado("CERTIFICADOB64...");
            comprobante.setNoCertificado("30001000000300023708");
            comprobante.setTipoDeComprobante(CTipoDeComprobante.I); // Ingreso
            comprobante.setExportacion("01"); // Operación nacional



            Emisor emisor = new Emisor();
            emisor.setRfc(dto.getRfcEmisor());
            emisor.setNombre(dto.getNombreEmisor());
            emisor.setRegimenFiscal("601"); // General de Ley Personas Morales
            comprobante.setEmisor(emisor);

            Receptor receptor = new Receptor();
            receptor.setRfc(dto.getRfcReceptor());
            receptor.setNombre(dto.getNombreReceptor());
            receptor.setUsoCFDI(dto.getUsoCfdi());
            receptor.setDomicilioFiscalReceptor("06000");
            receptor.setRegimenFiscalReceptor("605");
            comprobante.setReceptor(receptor);

            Conceptos conceptos = new Conceptos();
            BigDecimal total = BigDecimal.ZERO;

            for (BillRequestDTO.ConceptoDTO conceptoDTO : dto.getConceptos()) {
                Concepto concepto = new Concepto();
                concepto.setClaveProdServ(conceptoDTO.getClaveProdServ());
                concepto.setDescripcion(conceptoDTO.getDescripcion());
                concepto.setCantidad(BigDecimal.valueOf(conceptoDTO.getCantidad()));
                concepto.setUnidad(conceptoDTO.getUnidad());
                concepto.setValorUnitario(BigDecimal.valueOf(conceptoDTO.getValorUnitario()));
                concepto.setImporte(BigDecimal.valueOf(conceptoDTO.getImporte()));
                conceptos.getConcepto().add(concepto);
                concepto.setObjetoImp("01"); // <- esto es obligatorio para CFDI 4.0
                concepto.setClaveUnidad("H87"); // Ejemplo: H87 = Pieza
                total = total.add(BigDecimal.valueOf(conceptoDTO.getImporte()));
            }

            comprobante.setConceptos(conceptos);
            comprobante.setSubTotal(total);
            comprobante.setTotal(total);

            JAXBContext context = JAXBContext.newInstance(Comprobante.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw = new StringWriter();
            marshaller.marshal(comprobante, sw);

            return sw.toString();

        } catch (JAXBException e) {
            throw new RuntimeException("Error al generar el XML CFDI", e);
        }
    }


}
