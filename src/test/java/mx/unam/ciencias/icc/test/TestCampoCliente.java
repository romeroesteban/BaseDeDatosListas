package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import mx.unam.ciencias.icc.CampoCliente;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la enumeración {@link CampoCliente}.
 */
public class TestCampoCliente {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /**
     * Prueba unitaria para {@link CampoCliente#toString}.
     */
    @Test public void testToString() {
        String s = CampoCliente.NOMBRE.toString();
        Assert.assertTrue(s.equals("Nombre"));
        s = CampoCliente.EDAD.toString();
        Assert.assertTrue(s.equals("Edad"));
        s = CampoCliente.CORREO.toString();
        Assert.assertTrue(s.equals("Correo"));
        s = CampoCliente.TELEFONO.toString();
        Assert.assertTrue(s.equals("Teléfono"));
        s = CampoCliente.GASTO.toString();
        Assert.assertTrue(s.equals("Gasto"));
        s = CampoCliente.TRAMITES.toString();
        Assert.assertTrue(s.equals("Trámites"));
    }
}
