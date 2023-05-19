package mx.unam.ciencias.icc.red;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.Registro;

/**
 * Clase abstracta para servidores de bases de datos genéricas.
 */
public abstract class ServidorBaseDeDatos<R extends Registro<R, ?>> {

    /* La base de datos. */
    private BaseDeDatos<R, ?> bdd;
    /* La ruta donde cargar/guardar la base de datos. */
    private String ruta;
    /* El servidor de enchufes. */
    private ServerSocket servidor;
    /* El puerto. */
    private int puerto;
    /* Lista con las conexiones. */
    private Lista<Conexion<R>> conexiones;
    /* Bandera de continuación. */
    private boolean continuaEjecucion;
    /* Escuchas del servidor. */
    private Lista<EscuchaServidor> escuchas;

    /**
     * Crea un nuevo servidor usando la ruta recibida para poblar la base de
     * datos.
     * @param puerto el puerto dónde escuchar por conexiones.
     * @param ruta la ruta en el disco del cual cargar/guardar la base de
     *             datos. Puede ser <code>null</code>, en cuyo caso se usará el
     *             nombre por omisión <code>base-de-datos.bd</code>.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public ServidorBaseDeDatos(int puerto, String ruta)
        throws IOException {   
        this.puerto = puerto;
        if (ruta != null) {
            this.ruta = ruta;
        } else {
            this.ruta = "base-de-datos.bd";
        }
        
        servidor = new ServerSocket(puerto);
        // servidor.close();
        
        conexiones = new Lista<Conexion<R>>();
        escuchas = new Lista<EscuchaServidor>();
        this.bdd = creaBaseDeDatos();
        carga();
    }

    /**
     * Comienza a escuchar por conexiones de clientes.
     */
    public void sirve() {  
        continuaEjecucion = true;
        anotaMensaje("Escuchando en el puerto: " + String.valueOf(puerto) + ".");

        while (continuaEjecucion) {
            try {
                Socket enchufe = servidor.accept();
                Conexion<R> conexion = new Conexion<>(bdd, enchufe);
                String canonico = enchufe.getInetAddress().getCanonicalHostName();
                anotaMensaje("Conexión recibida de: " + canonico + ".");
                anotaMensaje("Serie de conexión: " + String.valueOf(conexion.getSerie()) + ".");
                conexion.agregaEscucha((f,a) -> mensajeRecibido(f,a));
                new Thread(() -> conexion.recibeMensajes()).start();

                synchronized (conexiones) {
                    conexiones.agregaFinal(conexion);
                }

            } catch (IOException ioe) {
                // if (continuaEjecucion)
                    anotaMensaje("Error al recibir una conexión...");
            }
        }
        anotaMensaje("La ejecución del servidor ha terminado.");

    }

    /**
     * Agrega un escucha de servidor.
     * @param escucha el escucha a agregar.
     */
    public void agregaEscucha(EscuchaServidor escucha) {  
        escuchas.agregaFinal(escucha);
    }

    /**
     * Limpia todos los escuchas del servidor.
     */
    public void limpiaEscuchas() {   
        escuchas.limpia();
    }

    /* Carga la base de datos del disco duro. */
    private void carga() {  
        if (bdd != null) {
            try {
                anotaMensaje("Cargando base de datos de " + String.valueOf(ruta) + ".");

                BufferedReader in =
                                new BufferedReader(
                                    new InputStreamReader(
                                        new FileInputStream(ruta)));
                bdd.carga(in);
                String linea = "";
                while((linea = in.readLine()) != null)
                    System.out.println(linea);
                in.close();

                anotaMensaje("Base de datos cargada exitosamente de " + String.valueOf(ruta) + ".");
                
            } catch (IOException ioe) {
                anotaMensaje("Ocurrió un error al tratar de cargar " + String.valueOf(ruta) + ".");
                anotaMensaje("La base de datos estará inicialmente vacía.");
            }
        } else {
            return;
        }
    }

    /* Guarda la base de datos en el disco duro. */
    private synchronized void guarda() {
        try {
            anotaMensaje("Guardando base de datos en " + String.valueOf(ruta) + ".");
            BufferedWriter out =
                                new BufferedWriter(
                                    new OutputStreamWriter(
                                        new FileOutputStream(ruta)));
            bdd.guarda(out);
            out.close();
            anotaMensaje("Base de datos guardada.");
        } catch (IOException ioe) {
            anotaMensaje("Ocurrió un error al guardar la base de datos.");
        }

    }

    /* Recibe los mensajes de la conexión. */
    private void mensajeRecibido(Conexion<R> conexion, Mensaje mensaje) {
        if (conexion.isActiva()) {
            switch(mensaje) {
                case BASE_DE_DATOS:
                    baseDeDatos(conexion);
                    break;
                case REGISTRO_AGREGADO:
                    registroAlterado(conexion, mensaje);
                    break;
                case REGISTRO_ELIMINADO:
                    registroAlterado(conexion, mensaje);
                    break;
                case REGISTRO_MODIFICADO:
                    registroModificado(conexion);
                    break;
                case DESCONECTAR:
                    desconectar(conexion);
                    break;
                case GUARDA:
                    guarda();
                    break;
                case DETENER_SERVICIO:
                    detenerServicio();
                    break;
                case ECO:
                    eco(conexion);
                    break;
                default:
                    // error(conexion, "error");
                    error(conexion, "Mensaje inválido.");
                    break;    
            }
        } else {
            return;
        }
    }

    /* Maneja el mensaje BASE_DE_DATOS. */
    private void baseDeDatos(Conexion<R> conexion) { 
        try {
            conexion.enviaMensaje(Mensaje.BASE_DE_DATOS);
            conexion.enviaBaseDeDatos();
        } catch (IOException ioe) {
            error(conexion, "Error enviando la base de datos.");
        }
        anotaMensaje("Base de datos pedida por " + String.valueOf(conexion.getSerie()) + ".");
    }

    /* Maneja los mensajes REGISTRO_AGREGADO y REGISTRO_MODIFICADO. */
    private void registroAlterado(Conexion<R> conexion, Mensaje mensaje) {
        R registro = null;
    	try {
    		registro = conexion.recibeRegistro();
    	} catch (IOException ioe) {
    		error(conexion, "Error recibiendo registro");
    	}    	
    	if (mensaje == Mensaje.REGISTRO_AGREGADO ) {
    		bdd.agregaRegistro(registro);
    		anotaMensaje("Registro agregado por " + conexion.getSerie() + ".");
    	} else {
    		bdd.eliminaRegistro(registro);
    		anotaMensaje("Registro eliminado por " + conexion.getSerie() + ".");
    	}
    	for (Conexion<R> c: conexiones) {
    		if (c == conexion)
    			continue;
    		try {
    			c.enviaMensaje(mensaje);
    			c.enviaRegistro(registro);    				
    		} catch (Exception e) {
    			error(conexion, "Error enviando registro");
    		}
    	}

        guarda();
    }

    /* Maneja el mensaje REGISTRO_MODIFICADO. */
    private void registroModificado(Conexion<R> conexion) {
        try {
            R r1 = conexion.recibeRegistro();
            R r2 = conexion.recibeRegistro();
            for (Conexion<R> c: conexiones) {
                if (c == conexion)
                    continue;
                try {
                    c.enviaMensaje(Mensaje.REGISTRO_MODIFICADO);
                    c.enviaRegistro(r1);
                    c.enviaRegistro(r2);
                } catch (IOException ioe) {
                    error(conexion, "Error enviando registros");
                }
            }
            modificaRegistro(r1, r2);
        } catch (IOException ioe) {
            error(conexion, "Error recibiendo registros");
            return;
        }

        anotaMensaje("Registro modificado por " + conexion.getSerie() + ".");
        guarda(); 
    }

    /* Maneja el mensaje DESCONECTAR. */
    private void desconectar(Conexion<R> conexion) {   
        anotaMensaje("Solicitud de desconexión por " + String.valueOf(conexion.getSerie()));
        desconecta(conexion);

    }

    /* Maneja el mensaje DETENER_SERVICIO. */
    private void detenerServicio() {  
        anotaMensaje("Solicitud para detener el servicio");
        continuaEjecucion = false;

        for (Conexion<R> conexionDistinta : conexiones) {
            conexionDistinta.desconecta();
                try {
                    servidor.close();
                } catch (IOException ioe) {}
        }
    }

    /* Maneja el mensaje ECO. */
    private void eco(Conexion<R> conexion) {  
        int serie = conexion.getSerie();
        anotaMensaje("Solicitud de eco de " + String.valueOf(conexion.getSerie()) + ".");
        try {
            conexion.enviaMensaje(Mensaje.ECO);    
        } catch(IOException ioe) {
            anotaMensaje("Error enviando eco");
        }
    }

    /* Imprime un mensaje a los escuchas y desconecta la conexión. */
    private void error(Conexion<R> conexion, String mensaje) {   
        anotaMensaje("Desconectando la conexión " + String.valueOf(conexion.getSerie()) + ": " + mensaje);
        desconecta(conexion);
    }

    /* Desconecta la conexión. */
    private void desconecta(Conexion<R> conexion) {
        synchronized (conexiones){
            conexiones.elimina(conexion);
            conexion.desconecta();
        }

        anotaMensaje("La conexión " + String.valueOf(conexion.getSerie()) + " ha sido desconectada.");
    }

    /* Agrega el registro a la base de datos. */
    private synchronized void agregaRegistro(R registro) {  
        bdd.agregaRegistro(registro);
    }

    /* Elimina el registro de la base de datos. */
    private synchronized void eliminaRegistro(R registro) {  
        bdd.eliminaRegistro(registro);
    }

    /* Modifica el registro en la base de datos. */
    private synchronized void modificaRegistro(R registro1, R registro2) { 
        bdd.modificaRegistro(registro1, registro2);
    }

    /* Procesa los mensajes de todos los escuchas. */
    private void anotaMensaje(String formato, Object ... argumentos) { 
        for(EscuchaServidor escucha : escuchas) {
            escucha.procesaMensaje(formato, argumentos);
        }
    }

    /**
     * Crea la base de datos concreta.
     * @return la base de datos concreta.
     */
    public abstract BaseDeDatos<R, ?> creaBaseDeDatos();
}
