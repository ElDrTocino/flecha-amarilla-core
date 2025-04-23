package org.flechaamarilla.util;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTimeConverter {
    public static XMLGregorianCalendar toXMLGregorianCalendar() {
        try {
            // Crear fecha actual sin milisegundos ni zona horaria
            LocalDateTime now = LocalDateTime.now();
            GregorianCalendar calendar = GregorianCalendar.from(now.atZone(TimeZone.getTimeZone("America/Mexico_City").toZoneId()));
            XMLGregorianCalendar xmlFecha = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);

            // Truncar los campos de milisegundos y zona horaria manualmente (los deja en null)
            xmlFecha.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
            xmlFecha.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
            return xmlFecha;
        } catch (Exception e) {
            throw new RuntimeException("Error converting LocalDateTime to XMLGregorianCalendar", e);
        }
    }
}
