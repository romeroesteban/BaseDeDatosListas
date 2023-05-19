package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosClientes;
import mx.unam.ciencias.icc.CampoCliente;
import mx.unam.ciencias.icc.Cliente;
import mx.unam.ciencias.icc.EscuchaBaseDeDatos;
import mx.unam.ciencias.icc.EventoBaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link BaseDeDatosClientes}.
 */
public class TestBaseDeDatosClientes {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Base de datos de clientes. */
    private BaseDeDatosClientes bdd;
    /* Número total de clientes. */
    private int total;

    /**
     * Crea un generador de números aleatorios para cada prueba y una base de
     * datos de clientes.
     */
    public TestBaseDeDatosClientes() {
        random = new Random();
        bdd = new BaseDeDatosClientes();
        total = 2 + random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosClientes#BaseDeDatosClientes}.
     */
    @Test public void testConstructor() {
        Lista<Cliente> clientes = bdd.getRegistros();
        Assert.assertFalse(clientes == null);
        Assert.assertTrue(clientes.getLongitud() == 0);
        Assert.assertTrue(bdd.getNumRegistros() == 0);
        bdd.agregaEscucha((e, r1, r2) -> {});
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getNumRegistros}.
     */
    @Test public void testGetNumRegistros() {
        Assert.assertTrue(bdd.getNumRegistros() == 0);
        for (int i = 0; i < total; i++) {
            Cliente c = TestCliente.clienteAleatorio();
            bdd.agregaRegistro(c);
            Assert.assertTrue(bdd.getNumRegistros() == i+1);
        }
        Assert.assertTrue(bdd.getNumRegistros() == total);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getRegistros}.
     */
    @Test public void testGetRegistros() {
        Lista<Cliente> l = bdd.getRegistros();
        Lista<Cliente> r = bdd.getRegistros();
        Assert.assertTrue(l.equals(r));
        Assert.assertFalse(l == r);
        Cliente[] clientes = new Cliente[total];
        for (int i = 0; i < total; i++) {
            clientes[i] = TestCliente.clienteAleatorio();
            bdd.agregaRegistro(clientes[i]);
        }
        l = bdd.getRegistros();
        int c = 0;
        for (Cliente cl : l)
            Assert.assertTrue(clientes[c++].equals(cl));
        l.elimina(clientes[0]);
        Assert.assertFalse(l.equals(bdd.getRegistros()));
        Assert.assertFalse(l.getLongitud() == bdd.getNumRegistros());
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#agregaRegistro}.
     */
    @Test public void testAgregaRegistro() {
        for (int i = 0; i < total; i++) {
            Cliente e = TestCliente.clienteAleatorio();
            Assert.assertFalse(bdd.getRegistros().contiene(e));
            bdd.agregaRegistro(e);
            Assert.assertTrue(bdd.getRegistros().contiene(e));
            Lista<Cliente> l = bdd.getRegistros();
            Assert.assertTrue(l.get(l.getLongitud() - 1).equals(e));
        }
        boolean[] llamado =  { false };
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_AGREGADO);
                Assert.assertTrue(r1.equals(new Cliente("A", 1, "@", 1, 1, 1)));
                Assert.assertTrue(r2 == null);
                llamado[0] = true;
            });
        bdd.agregaRegistro(new Cliente("A", 1, "@", 1, 1, 1));
        Assert.assertTrue(llamado[0]);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#eliminaRegistro}.
     */
    @Test public void testEliminaRegistro() {
        int ini = random.nextInt(1000000);
        for (int i = 0; i < total; i++) {
            Cliente e = TestCliente.clienteAleatorio(ini + i);
            bdd.agregaRegistro(e);
        }
        while (bdd.getNumRegistros() > 0) {
            int i = random.nextInt(bdd.getNumRegistros());
            Cliente e = bdd.getRegistros().get(i);
            Assert.assertTrue(bdd.getRegistros().contiene(e));
            bdd.eliminaRegistro(e);
            Assert.assertFalse(bdd.getRegistros().contiene(e));
        }
        boolean[] llamado = { false };
        Cliente cliente = new Cliente("A", 1, "@", 1, 1, 1);
        bdd.agregaRegistro(cliente);
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_ELIMINADO);
                Assert.assertTrue(r1.equals(new Cliente("A", 1, "@", 1, 1, 1)));
                Assert.assertTrue(r2 == null);
                llamado[0] = true;
            });
        bdd.eliminaRegistro(cliente);
        Assert.assertTrue(llamado[0]);
        bdd = new BaseDeDatosClientes();
        llamado[0] = false;
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_ELIMINADO);
                Assert.assertTrue(r1.equals(new Cliente("A", 1, "@", 1, 1, 1)));
                Assert.assertTrue(r2 == null);
                llamado[0] = true;
            });
        bdd.eliminaRegistro(cliente);
        Assert.assertTrue(llamado[0]);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#modificaRegistro}.
     */
    @Test public void testModificaRegistro() {
        for (int i = 0; i < total; i++) {
            Cliente e = TestCliente.clienteAleatorio(total + i);
            Assert.assertFalse(bdd.getRegistros().contiene(e));
            bdd.agregaRegistro(e);
            Assert.assertTrue(bdd.getRegistros().contiene(e));
            Lista<Cliente> l = bdd.getRegistros();
            Assert.assertTrue(l.get(l.getLongitud() - 1).equals(e));
        }
        Cliente a = new Cliente("A", 1, "@gmail", 1, 1, 1);
        Cliente b = new Cliente("B", 2, "@hotmail", 2, 2, 2);
        bdd.agregaRegistro(a);
        boolean[] llamado = { false };
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_MODIFICADO);
                Assert.assertTrue(r1.equals(new Cliente("A", 1, "@gmail", 1, 1, 1)));
                Assert.assertTrue(r2.equals(new Cliente("B", 2, "@hotmail", 2, 2, 2)));
                llamado[0] = true;
            });
        Cliente c = new Cliente("A", 1, "@gmail", 1, 1, 1);
        bdd.modificaRegistro(c, b);
        Assert.assertTrue(llamado[0]);
        Assert.assertTrue(c.equals(new Cliente("A", 1, "@gmail", 1, 1, 1)));
        Assert.assertTrue(b.equals(new Cliente("B", 2, "@hotmail", 2, 2, 2)));
        int ca = 0, cb = 0;
        for (Cliente e : bdd.getRegistros()) {
            ca += e.equals(c) ? 1 : 0;
            cb += e.equals(b) ? 1 : 0;
        }
        Assert.assertTrue(ca == 0);
        Assert.assertTrue(cb == 1);
        bdd = new BaseDeDatosClientes();
        a = new Cliente("A", 1, "@gmail", 1, 1, 1);
        b = new Cliente("B", 2, "@hotmail", 2, 2, 2);
        bdd.agregaRegistro(a);
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_MODIFICADO);
                Assert.assertTrue(r1.equals(new Cliente("A", 1, "@gmail", 1, 1, 1)));
                Assert.assertTrue(r2.equals(new Cliente("B", 2, "@hotmail", 2, 2, 2)));
                llamado[0] = true;
            });
        bdd.modificaRegistro(a, b);
        Assert.assertTrue(a.equals(b));
        Assert.assertTrue(llamado[0]);
        bdd = new BaseDeDatosClientes();
        llamado[0] = false;
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_MODIFICADO);
                llamado[0] = true;
            });
        bdd.modificaRegistro(new Cliente("A", 1, "@gmail", 1, 1, 1),
                             new Cliente("B", 2, "@hotmail", 2, 2, 2));
        Assert.assertFalse(llamado[0]);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#limpia}.
     */
    @Test public void testLimpia() {
        for (int i = 0; i < total; i++) {
            Cliente e = TestCliente.clienteAleatorio();
            bdd.agregaRegistro(e);
        }
        boolean[] llamado = { false };
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.BASE_LIMPIADA);
                Assert.assertTrue(r1 == null);
                Assert.assertTrue(r2 == null);
                llamado[0] = true;
            });
        Lista<Cliente> registros = bdd.getRegistros();
        Assert.assertFalse(registros.esVacia());
        Assert.assertFalse(registros.getLongitud() == 0);
        bdd.limpia();
        registros = bdd.getRegistros();
        Assert.assertTrue(registros.esVacia());
        Assert.assertTrue(registros.getLongitud() == 0);
        Assert.assertTrue(llamado[0]);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#guarda}.
     */
    @Test public void testGuarda() {
        for (int i = 0; i < total; i++) {
            Cliente e = TestCliente.clienteAleatorio();
            bdd.agregaRegistro(e);
        }
        String guardado = "";
        try {
            StringWriter swOut = new StringWriter();
            BufferedWriter out = new BufferedWriter(swOut);
            bdd.guarda(out);
            out.close();
            guardado = swOut.toString();
        } catch (IOException ioe) {
            Assert.fail();
        }
        String[] lineas = guardado.split("\n");
        Assert.assertTrue(lineas.length == total);
        Lista<Cliente> l = bdd.getRegistros();
        int c = 0;
        for (Cliente e : l) {
            String el = String.format("%s\t%d\t%s\t%d\t%2.2f\t%d",
                                     e.getNombre(), 
                                     e.getEdad(), 
                                     e.getCorreo(), 
                                     e.getTelefono(),
                                     e.getGasto(), 
                                     e.getTramites());
            Assert.assertTrue(lineas[c++].equals(el));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#carga}.
     */
    @Test public void testCarga() {
        int ini = random.nextInt(1000000);
        String entrada = "";
        Cliente[] clientes = new Cliente[total];
        for (int i = 0; i < total; i++) {
            clientes[i] = TestCliente.clienteAleatorio(ini + i);
            entrada += String.format("%s\t%d\t%s\t%d\t%2.2f\t%d\n",
                                     clientes[i].getNombre(), 
                                     clientes[i].getEdad(), 
                                     clientes[i].getCorreo(), 
                                     clientes[i].getTelefono(),
                                     clientes[i].getGasto(), 
                                     clientes[i].getTramites());
            bdd.agregaRegistro(clientes[i]);
        }
        int[] contador = { 0 };
        boolean[] llamado = { false };
        bdd.agregaEscucha((e, r1, r2) -> {
                if (e == EventoBaseDeDatos.BASE_LIMPIADA)
                    llamado[0] = true;
                if (e == EventoBaseDeDatos.REGISTRO_AGREGADO) {
                    contador[0] ++;
                    Assert.assertTrue(r1 != null);
                    Assert.assertTrue(r2 == null);
                }
            });
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Lista<Cliente> l = bdd.getRegistros();
        Assert.assertTrue(l.getLongitud() == total);
        int c = 0;
        for (Cliente e : l)
            Assert.assertTrue(clientes[c++].equals(e));
        Assert.assertTrue(llamado[0]);
        Assert.assertTrue(contador[0] == total);
        contador[0] = 0;
        llamado[0] = false;
        entrada = String.format("%s\t%d\t%s\t%d\t%2.2f\t%d\n",
                                     clientes[0].getNombre(), 
                                     clientes[0].getEdad(), 
                                     clientes[0].getCorreo(), 
                                     clientes[0].getTelefono(),
                                     clientes[0].getGasto(), 
                                     clientes[0].getTramites());
        entrada += " \n";
        entrada += String.format("%s\t%d\t%s\t%d\t%2.2f\t%d\n",
                                     clientes[1].getNombre(), 
                                     clientes[1].getEdad(), 
                                     clientes[1].getCorreo(), 
                                     clientes[1].getTelefono(),
                                     clientes[1].getGasto(), 
                                     clientes[1].getTramites());
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Assert.assertTrue(bdd.getNumRegistros() == 1);
        Assert.assertTrue(llamado[0]);
        Assert.assertTrue(contador[0] == 1);
        entrada = "";
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatosClientes#creaRegistro}.
     */
    @Test public void testCreaRegistro() {
        Cliente c = bdd.creaRegistro();
        Assert.assertTrue(c.getNombre() == null);
        Assert.assertTrue(c.getEdad() == 0);
        Assert.assertTrue(c.getCorreo() == null);
        Assert.assertTrue(c.getTelefono() == 0);
        Assert.assertTrue(c.getGasto() == 0.0);
        Assert.assertTrue(c.getTramites() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatosClientes#buscaRegistros}.
     */
    @Test public void testBuscaRegistros() {
        Cliente[] clientes = new Cliente[total];
        int ini = 1000000 + random.nextInt(999999);
        for (int i = 0; i < total; i++) {
            Cliente e =  new Cliente(String.valueOf(ini+i),
                                           i, String.valueOf(ini+i),
                                           ini+i, (i * 10.0) / total, i);
            clientes[i] = e;
            bdd.agregaRegistro(e);
        }

        Cliente cliente;
        Lista<Cliente> l;
        int i;

        for (int k = 0; k < total/10 + 3; k++) {
            i = random.nextInt(total);
            cliente = clientes[i];

            String nombre = cliente.getNombre();
            l = bdd.buscaRegistros(CampoCliente.NOMBRE, nombre);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getNombre().indexOf(nombre) > -1);
            int n = nombre.length();
            String bn = nombre.substring(random.nextInt(2),
                                         2 + random.nextInt(n-2));
            l = bdd.buscaRegistros(CampoCliente.NOMBRE, bn);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getNombre().indexOf(bn) > -1);

            int edad = cliente.getEdad();
            l = bdd.buscaRegistros(CampoCliente.EDAD, edad);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getEdad() >= edad);
            int be = edad - 10;
            l = bdd.buscaRegistros(CampoCliente.EDAD, be);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getEdad() >= be);

            String correo = cliente.getCorreo();
            l = bdd.buscaRegistros(CampoCliente.CORREO, correo);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getCorreo().indexOf(correo) > -1);
            int co = correo.length();
            String bc = correo.substring(random.nextInt(2),
                                         2 + random.nextInt(co-2));
            l = bdd.buscaRegistros(CampoCliente.CORREO, bc);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getCorreo().indexOf(bc) > -1);
            
            int telefono = cliente.getTelefono();
            l = bdd.buscaRegistros(CampoCliente.TELEFONO, telefono);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getTelefono() >= telefono);
            int bt = telefono - 10;
            l = bdd.buscaRegistros(CampoCliente.TELEFONO, bt);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getTelefono() >= bt);

            double gasto = cliente.getGasto();
            l = bdd.buscaRegistros(CampoCliente.GASTO, gasto);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getGasto() >= gasto);
            double bg = gasto - 5.0;
            l = bdd.buscaRegistros(CampoCliente.GASTO, bg);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getGasto() >= bg);

            int tramites = cliente.getTramites();
            l = bdd.buscaRegistros(CampoCliente.TRAMITES, tramites);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getTramites() >= tramites);
            int btr = tramites - 1;
            l = bdd.buscaRegistros(CampoCliente.TRAMITES, btr);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(cliente));
            for (Cliente c : l)
                Assert.assertTrue(c.getEdad() >= btr);
        }

        l = bdd.buscaRegistros(CampoCliente.NOMBRE,
                               "xxx-nombre");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.EDAD, 127);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.CORREO,
                               "@xxx");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.TELEFONO, 9123456);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.GASTO, 97000000.12);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.TRAMITES, 1270);
        Assert.assertTrue(l.esVacia());

        l = bdd.buscaRegistros(CampoCliente.NOMBRE, "");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.EDAD, Integer.MAX_VALUE);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.CORREO, "");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.TELEFONO, Integer.MAX_VALUE);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.GASTO, Double.MAX_VALUE);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.TRAMITES, Integer.MAX_VALUE);
        Assert.assertTrue(l.esVacia());

        l = bdd.buscaRegistros(CampoCliente.NOMBRE, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.EDAD, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.CORREO, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.TELEFONO, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.GASTO, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoCliente.TRAMITES, null);
        Assert.assertTrue(l.esVacia());

        try {
            l = bdd.buscaRegistros(null, null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#agregaEscucha}.
     */
    @Test public void testAgregaEscucha() {
        int[] c = new int[total];
        for (int i = 0; i < total; i++) {
            final int j = i;
            bdd.agregaEscucha((e, r1, r2) -> c[j]++);
        }
        bdd.agregaRegistro(new Cliente("A", 1, "@", 1, 1, 1));
        for (int i = 0; i < total; i++)
            Assert.assertTrue(c[i] == 1);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#eliminaEscucha}.
     */
    @Test public void testEliminaEscucha() {
        int[] c = new int[total];
        Lista<EscuchaBaseDeDatos<Cliente>> escuchas =
            new Lista<EscuchaBaseDeDatos<Cliente>>();
        for (int i = 0; i < total; i++) {
            final int j = i;
            EscuchaBaseDeDatos<Cliente> escucha = (e, r1, r2) -> c[j]++;
            bdd.agregaEscucha(escucha);
            escuchas.agregaFinal(escucha);
        }
        int i = 0;
        while (!escuchas.esVacia()) {
            bdd.agregaRegistro(TestCliente.clienteAleatorio(i++));
            EscuchaBaseDeDatos<Cliente> escucha = escuchas.eliminaPrimero();
            bdd.eliminaEscucha(escucha);
        }
        for (i = 0; i < total; i++)
            Assert.assertTrue(c[i] == i + 1);
    }
}
