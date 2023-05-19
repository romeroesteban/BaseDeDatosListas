package mx.unam.ciencias.icc.red;

import java.io.IOException;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosClientes;
import mx.unam.ciencias.icc.CampoCliente;
import mx.unam.ciencias.icc.Cliente;

/**
 * Clase para servidores de bases de datos de clientes.
 */
public class ServidorBaseDeDatosClientes
    extends ServidorBaseDeDatos<Cliente> {

    /**
     * Construye un servidor de base de datos de clientes.
     * @param puerto el puerto dónde escuchar por conexiones.
     * @param archivo el archivo en el disco del cual cargar/guardar la base de
     *                datos.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public ServidorBaseDeDatosClientes(int puerto, String archivo)
        throws IOException { 
        super(puerto, archivo);
    }

    /**
     * Crea una base de datos de clientes.
     * @return una base de datos de clientes.
     */
    @Override public
    BaseDeDatos<Cliente, CampoCliente> creaBaseDeDatos() {  
        return new BaseDeDatosClientes();
    }
}
