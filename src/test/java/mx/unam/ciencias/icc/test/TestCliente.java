package mx.unam.ciencias.icc.test;

import java.util.Random;
import mx.unam.ciencias.icc.CampoCliente;
import mx.unam.ciencias.icc.Cliente;
import mx.unam.ciencias.icc.ExcepcionLineaInvalida;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Cliente}.
 */
public class TestCliente {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Nombres. */
    private static final String[] NOMBRES = {
        "Alejandro", "Ari", "Yorgos", "Andréi", "Luis",
        "Gaspar", "Lars", "Pedro", "Krzysztof", "Wong"
    };

    /* Apellidos. */
    private static final String[] APELLIDOS = {
        "González Iñárritu", "Aster", "Lanthimos", "Tarkovski", "Buñuel",
        "Noé", "Von Trier", "Almodóvar", "Kieslowski", "Kar Wai"
    };

    /* Dominios. */
    private static final String[] DOMINIOS = {
        "gmail.com", "hotmail.com", "ciencias.unam.mx", "outlook.com", "aol.com",
        "yahoo.com", "icloud.com", "protonmail.com", "zoho.com", "prodigy.net"
    };

    /* Palabras. */
    private static final String[] PALABRAS = {
        "picarasonadora", "defenestrador", "slipknot", "naruto", "flor",
        "gatito", "metallica"
    };

    /* Generador de números aleatorios. */
    private static Random random;

    /**
     * Genera un nombre aleatorio.
     * @return un nombre aleatorio.
     */
    public static String nombreAleatorio() {
        int n = random.nextInt(NOMBRES.length);
        int ap = random.nextInt(APELLIDOS.length);
        int am = random.nextInt(APELLIDOS.length);
        return NOMBRES[n] + " " + APELLIDOS[ap] + " " + APELLIDOS[am];
    }

    /**
     * Genera una edad aleatoria.
     * @return una edad aleatoria.
     */
    public static int edadAleatoria() {
        return 17 + random.nextInt(73);
    }

    /**
     * Genera un correo aleatorio.
     * @return un correo aleatorio.
     */
    public static String correoAleatorio() {
        int n = random.nextInt(PALABRAS.length);
        int am = random.nextInt(DOMINIOS.length);
        return PALABRAS[n] + String.valueOf(random.nextInt(500)) + "@" + DOMINIOS[am];
    }

    /**
     * Genera un número de teléfono aleatorio.
     * @return un número de teléfono aleatorio.
     */
    public static int telefonoAleatorio() {
        return 10000000 + random.nextInt(10000000);
    }

    /**
     * Genera un gasto aleatorio.
     * @return un gasto aleatorio.
     */
    public static double gastoAleatorio() {
        /* Estúpida precisión. */
        return random.nextInt(1000000) / 1.0;
    }

    /**
     * Genera un número de trámites aleatorio.
     * @return un número de trámites aleatorio.
     */
    public static int tramitesAleatorio() {
        return random.nextInt(20);
    }

    /**
     * Genera un cliente aleatorio.
     * @return un cliente aleatorio.
     */
    public static Cliente clienteAleatorio() {
        return new Cliente(nombreAleatorio(),
                              edadAleatoria(),
                              correoAleatorio(),
                              telefonoAleatorio(),
                              gastoAleatorio(),
                              tramitesAleatorio());
    }

    /**
     * Genera un cliente aleatorio con un número telefónico dado.
     * @param telefono el número telefónico del nuevo cliente.
     * @return un cliente aleatorio.
     */
    public static Cliente clienteAleatorio(int telefono) {
        return new Cliente(nombreAleatorio(),
                              edadAleatoria(),
                              correoAleatorio(),
                              telefono,
                              gastoAleatorio(),
                              tramitesAleatorio());
    }

    /* El cliente. */
    private Cliente cliente;

    /**
     * Prueba unitaria para {@link
     * Clienteante#Clienteante(String,int,double,int)}.
     */
    @Test public void testConstructor() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, telefono, gasto, tramites);
        Assert.assertTrue(cliente.getNombre().equals(nombre));
        Assert.assertTrue(cliente.getEdad() == edad);
        Assert.assertTrue(cliente.getCorreo().equals(correo));
        Assert.assertTrue(cliente.getTelefono() == telefono);
        Assert.assertTrue(cliente.getGasto() == gasto);
        Assert.assertTrue(cliente.getTramites() == tramites);
    }

    /**
     * Prueba unitaria para {@link Cliente#getNombre}.
     */
    @Test public void testGetNombre() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getNombre().equals(nombre));
        Assert.assertFalse(cliente.getNombre().equals(nombre + " X"));
    }

    /**
     * Prueba unitaria para {@link Cliente#setNombre}.
     */
    @Test public void testSetNombre() {
        String nombre = nombreAleatorio();
        String nuevoNombre = nombre + " X";
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getNombre().equals(nombre));
        Assert.assertFalse(cliente.getNombre().equals(nuevoNombre));
        cliente.setNombre(nuevoNombre);
        Assert.assertFalse(cliente.getNombre().equals(nombre));
        Assert.assertTrue(cliente.getNombre().equals(nuevoNombre));
    }

    /**
     * Prueba unitaria para {@link Cliente#nombreProperty}.
     */
    @Test public void testNombreProperty() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.nombreProperty().get().equals(nombre));
    }

    /**
     * Prueba unitaria para {@link Cliente#getEdad}.
     */
    @Test public void testGetEdad() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getEdad() == edad);
        Assert.assertFalse(cliente.getEdad() == edad + 10);
    }

    /**
     * Prueba unitaria para {@link Cliente#setEdad}.
     */
    @Test public void testSetEdad() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        int nuevaEdad = edad + 5;
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getEdad() == edad);
        Assert.assertFalse(cliente.getEdad() == edad + 5);
        cliente.setEdad(nuevaEdad);
        Assert.assertFalse(cliente.getEdad() == edad);
        Assert.assertTrue(cliente.getEdad() == nuevaEdad);
    }

    /**
     * Prueba unitaria para {@link Cliente#edadProperty}.
     */
    @Test public void testEdadProperty() {
         String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.edadProperty().get() == edad);
    }

    /**
     * Prueba unitaria para {@link Cliente#getCorreo}.
     */
    @Test public void testGetCorreo() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getCorreo().equals(correo));
        Assert.assertFalse(cliente.getCorreo().equals(correo + " X"));
    }

    /**
     * Prueba unitaria para {@link Cliente#setCorreo}.
     */
    @Test public void testSetCorreo() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        String nuevoCorreo = correo + " .mx";
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getCorreo().equals(correo));
        Assert.assertFalse(cliente.getCorreo().equals(nuevoCorreo));
        cliente.setCorreo(nuevoCorreo);
        Assert.assertFalse(cliente.getCorreo().equals(correo));
        Assert.assertTrue(cliente.getCorreo().equals(nuevoCorreo));
    }

    /**
     * Prueba unitaria para {@link Cliente#correoProperty}.
     */
    @Test public void testCorreoProperty() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.correoProperty().get().equals(correo));
    }

    /**
     * Prueba unitaria para {@link Cliente#getTelefono}.
     */
    @Test public void testGetTelefono() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getTelefono() == telefono);
        Assert.assertFalse(cliente.getTelefono() == telefono + 2);
    }

    /**
     * Prueba unitaria para {@link Cliente#setTelefono}.
     */
    @Test public void testSetTelefono() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        int nuevoTelefono = telefono + 2;
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getTelefono() == telefono);
        Assert.assertFalse(cliente.getTelefono() == telefono + 2);
        cliente.setTelefono(nuevoTelefono);
        Assert.assertFalse(cliente.getTelefono() == telefono);
        Assert.assertTrue(cliente.getTelefono() == nuevoTelefono);
    }

    /**
     * Prueba unitaria para {@link Cliente#telefonoProperty}.
     */
    @Test public void testTelefonoProperty() {
         String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.telefonoProperty().get() == telefono);
    }

    /**
     * Prueba unitaria para {@link Cliente#getGasto}.
     */
    @Test public void testGetGasto() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getGasto() == gasto);
        Assert.assertFalse(cliente.getGasto() == gasto + 150.5);
    }

    /**
     * Prueba unitaria para {@link Cliente#setGasto}.
     */
    @Test public void testSetGasto() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        double nuevoGasto = gasto + 150.5;
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getGasto() == gasto);
        Assert.assertFalse(cliente.getGasto() == nuevoGasto);
        cliente.setGasto(nuevoGasto);
        Assert.assertFalse(cliente.getGasto() == gasto);
        Assert.assertTrue(cliente.getGasto() == nuevoGasto);
    }

    /**
     * Prueba unitaria para {@link Cliente#gastoProperty}.
     */
    @Test public void testGastoProperty() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.gastoProperty().get() == gasto);
    }

    /**
     * Prueba unitaria para {@link Cliente#getTramites}.
     */
    @Test public void testGetTramites() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getTramites() == tramites);
        Assert.assertFalse(cliente.getTramites() == tramites + 5);
    }

    /**
     * Prueba unitaria para {@link Cliente#setTramites}.
     */
    @Test public void testSetTramites() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        int nuevoTramites = tramites + 5;
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.getTramites() == tramites);
        Assert.assertFalse(cliente.getTramites() == nuevoTramites);
        cliente.setTramites(nuevoTramites);
        Assert.assertFalse(cliente.getTramites() == tramites);
        Assert.assertTrue(cliente.getTramites() == nuevoTramites);
    }

    /**
     * Prueba unitaria para {@link Cliente#tramitesProperty}.
     */
    @Test public void testTramitesProperty() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Assert.assertTrue(cliente.tramitesProperty().get() == tramites);
    }

    /**
     * Prueba unitaria para {@link Cliente#toString}.
     */
    @Test public void testToString() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        String cadena = String.format("Nombre   : %s\n" +
                                      "Edad     : %d\n" +
                                      "Correo   : %s\n" +
                                      "Teléfono : %10d\n" +
                                      "Gasto    : %2.2f\n" +
                                      "Trámites : %d",
                                      nombre, edad, correo, 
                                      telefono, gasto, tramites);
        Assert.assertTrue(cliente.toString().equals(cadena));
        telefono = 331825396;
        gasto = 163.9;
        cliente.setTelefono(telefono);
        cliente.setGasto(gasto);
        cadena = String.format("Nombre   : %s\n" +
                                      "Edad     : %d\n" +
                                      "Correo   : %s\n" +
                                      "Teléfono : %10d\n" +
                                      "Gasto    : %2.2f\n" +
                                      "Trámites : %d",
                                      nombre, edad, correo, 
                                      telefono, gasto, tramites);
        Assert.assertTrue(cliente.toString().equals(cadena));
    }

    /**
     * Prueba unitaria para {@link Cliente#equals}.
     */
    @Test public void testEquals() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        Cliente igual = new Cliente(new String(nombre), edad, correo,
                                          telefono, gasto, tramites);
        Assert.assertTrue(cliente.equals(igual));
        String otroNombre = nombre + " Segundo";
        int otraEdad = edad + 1;
        String otroCorreo = correo + ".mx";
        int otroTelefono = telefono + 2;
        double otroGasto = (gasto != 0.0) ? gasto / 10.0 : 10.0;
        int otroTramites = tramites + 1;
        Cliente distinto =
            new Cliente(otroNombre, edad, correo, telefono, gasto, tramites);
        Assert.assertFalse(cliente.equals(distinto));
        distinto = new Cliente(nombre, otraEdad, correo, telefono, gasto, tramites);
        Assert.assertFalse(cliente.equals(distinto));
        distinto = new Cliente(nombre, edad, otroCorreo, telefono, gasto, tramites);
        Assert.assertFalse(cliente.equals(distinto));
        distinto = new Cliente(nombre, edad, correo, otroTelefono, gasto, tramites);
        Assert.assertFalse(cliente.equals(distinto));
        distinto = new Cliente(nombre, edad, correo, telefono, otroGasto, tramites);
        Assert.assertFalse(cliente.equals(distinto));
        distinto = new Cliente(nombre, edad, correo, telefono, gasto, otroTramites);
        Assert.assertFalse(cliente.equals(distinto));
        distinto = new Cliente(otroNombre, otraEdad, otroCorreo, 
                               otroTelefono, otroGasto, otroTramites);
        Assert.assertFalse(cliente.equals(distinto));
        Assert.assertFalse(cliente.equals("Una cadena"));
        Assert.assertFalse(cliente.equals(null));
    }

    /**
     * Prueba unitaria para {@link Cliente#seria}.
     */
    @Test public void testSeria() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, 
                                telefono, gasto, tramites);
        String linea = String.format("%s\t%d\t%s\t%d\t%2.2f\t%d\n",
                                     nombre, edad, correo, telefono,
                                     gasto, tramites);
        Assert.assertTrue(cliente.seria().equals(linea));
    }

    /**
     * Prueba unitaria para {@link Cliente#deseria}.
     */
    @Test public void testDeseria() {
        cliente = new Cliente(null, 0, null, 0, 0.0, 0);

        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();

        String linea = String.format("%s\t%d\t%s\t%d\t%2.2f\t%d\n",
                                     nombre, edad, correo, telefono,
                                     gasto, tramites);

        try {
            cliente.deseria(linea);
        } catch (ExcepcionLineaInvalida eli) {
            Assert.fail();
        }

        Assert.assertTrue(cliente.getNombre().equals(nombre));
        Assert.assertTrue(cliente.getEdad() == edad);
        Assert.assertTrue(cliente.getCorreo().equals(correo));
        Assert.assertTrue(cliente.getTelefono() == telefono);
        Assert.assertTrue(cliente.getGasto() == gasto);
        Assert.assertTrue(cliente.getTramites() == tramites);

        String[] invalidas = {"", " ", "\t", "  ", "\t\t",
                              " \t", "\t ", "\n", "a\ta\ta",
                              "a\ta\ta\ta"};

        for (int i = 0; i < invalidas.length; i++) {
            linea = invalidas[i];
            try {
                cliente.deseria(linea);
                Assert.fail();
            } catch (ExcepcionLineaInvalida eli) {}
        }
    }

    /**
     * Prueba unitaria para {@link Cliente#actualiza}.
     */
    @Test public void testActualiza() {
        cliente = new Cliente("A", 1, "@", 1, 1, 1);
        Cliente c = new Cliente("B", 2, "@", 2, 2, 2);
        Assert.assertFalse(cliente == c);
        Assert.assertFalse(cliente.equals(c));
        cliente.actualiza(c);
        Assert.assertFalse(cliente == c);
        Assert.assertTrue(cliente.equals(c));
        Assert.assertTrue(cliente.getNombre().equals("B"));
        Assert.assertFalse(cliente.nombreProperty() ==
                           c.nombreProperty());
        Assert.assertFalse(cliente.edadProperty() ==
                           c.edadProperty());
        Assert.assertFalse(cliente.correoProperty() ==
                           c.correoProperty());
        Assert.assertFalse(cliente.telefonoProperty() ==
                           c.telefonoProperty());
        Assert.assertFalse(cliente.gastoProperty() ==
                           c.gastoProperty());
        Assert.assertFalse(cliente.tramitesProperty() ==
                           c.tramitesProperty());
        try {
            cliente.actualiza(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Cliente#casa}.
     */
    @Test public void testCasa() {
        String nombre = nombreAleatorio();
        int edad = edadAleatoria();
        String correo = correoAleatorio();
        int telefono = telefonoAleatorio();
        double gasto = gastoAleatorio();
        int tramites = tramitesAleatorio();
        cliente = new Cliente(nombre, edad, correo, telefono, gasto, tramites);

        String n = cliente.getNombre();
        int m = cliente.getNombre().length();
        Assert.assertTrue(cliente.casa(CampoCliente.NOMBRE, n));
        n = cliente.getNombre().substring(0, m/2);
        Assert.assertTrue(cliente.casa(CampoCliente.NOMBRE, n));
        n = cliente.getNombre().substring(m/2, m);
        Assert.assertTrue(cliente.casa(CampoCliente.NOMBRE, n));
        n = cliente.getNombre().substring(m/3, 2*(m/3));
        Assert.assertTrue(cliente.casa(CampoCliente.NOMBRE, n));
        Assert.assertFalse(cliente.casa(CampoCliente.NOMBRE, ""));
        Assert.assertFalse(cliente.casa(CampoCliente.NOMBRE, "XXX"));
        Assert.assertFalse(cliente.casa(CampoCliente.NOMBRE, 1000));
        Assert.assertFalse(cliente.casa(CampoCliente.NOMBRE, null));

        int e = cliente.getEdad();
        Assert.assertTrue(cliente.casa(CampoCliente.EDAD, e));
        e = cliente.getEdad() - 10;
        Assert.assertTrue(cliente.casa(CampoCliente.EDAD, e));
        e = cliente.getEdad() + 10;
        Assert.assertFalse(cliente.casa(CampoCliente.EDAD, e));
        Assert.assertFalse(cliente.casa(CampoCliente.EDAD, "XXX"));
        Assert.assertFalse(cliente.casa(CampoCliente.EDAD, null));

        String c = cliente.getCorreo();
        int p = cliente.getCorreo().length();
        Assert.assertTrue(cliente.casa(CampoCliente.CORREO, c));
        c = cliente.getCorreo().substring(0, p/2);
        Assert.assertTrue(cliente.casa(CampoCliente.CORREO, c));
        c = cliente.getCorreo().substring(p/2, p);
        Assert.assertTrue(cliente.casa(CampoCliente.CORREO, c));
        c = cliente.getCorreo().substring(p/3, 2*(p/3));
        Assert.assertTrue(cliente.casa(CampoCliente.CORREO, c));
        Assert.assertFalse(cliente.casa(CampoCliente.CORREO, ""));
        Assert.assertFalse(cliente.casa(CampoCliente.CORREO, "XXX"));
        Assert.assertFalse(cliente.casa(CampoCliente.CORREO, 1000));
        Assert.assertFalse(cliente.casa(CampoCliente.CORREO, null));

        int t = cliente.getTelefono();
        Assert.assertTrue(cliente.casa(CampoCliente.TELEFONO, t));
        t = cliente.getTelefono() - 2;
        Assert.assertTrue(cliente.casa(CampoCliente.TELEFONO, t));
        t = cliente.getTelefono() + 2;
        Assert.assertFalse(cliente.casa(CampoCliente.TELEFONO, t));
        Assert.assertFalse(cliente.casa(CampoCliente.TELEFONO, "XXX"));
        Assert.assertFalse(cliente.casa(CampoCliente.TELEFONO, null));

        double g = cliente.getGasto();
        Assert.assertTrue(cliente.casa(CampoCliente.GASTO, g));
        g = cliente.getGasto() - 50.0;
        Assert.assertTrue(cliente.casa(CampoCliente.GASTO, g));
        g = cliente.getGasto() + 50.0;
        Assert.assertFalse(cliente.casa(CampoCliente.GASTO, g));
        Assert.assertFalse(cliente.casa(CampoCliente.GASTO, "XXX"));
        Assert.assertFalse(cliente.casa(CampoCliente.GASTO, null));

        int tr = cliente.getTramites();
        Assert.assertTrue(cliente.casa(CampoCliente.TRAMITES, tr));
        tr = cliente.getTramites() - 2;
        Assert.assertTrue(cliente.casa(CampoCliente.TRAMITES, tr));
        tr = cliente.getTramites() + 2;
        Assert.assertFalse(cliente.casa(CampoCliente.TRAMITES, tr));
        Assert.assertFalse(cliente.casa(CampoCliente.TRAMITES, "XXX"));
        Assert.assertFalse(cliente.casa(CampoCliente.TRAMITES, null));

        try {
            cliente.casa(null, null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /* Inicializa el generador de números aleatorios. */
    static {
        random = new Random();
    }
}
