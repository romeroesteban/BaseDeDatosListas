package mx.unam.ciencias.icc;

/**
 * Clase para bases de datos de clientes.
 */
public class BaseDeDatosClientes
    extends BaseDeDatos<Cliente, CampoCliente> {

    /**
     * Crea un cliente en blanco.
     * @return un cliente en blanco.
     */
    @Override public Cliente creaRegistro() {
        Cliente cliente = new Cliente(null, 0, null, 0, 0.0, 0);
        return cliente;
    }
}
