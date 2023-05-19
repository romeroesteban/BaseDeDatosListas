package mx.unam.ciencias.icc.igu;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.BaseDeDatosClientes;
import mx.unam.ciencias.icc.Cliente;
import mx.unam.ciencias.icc.EventoBaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.red.Conexion;
import mx.unam.ciencias.icc.red.Mensaje;

/**
 * Clase para el controlador de la ventana principal de la aplicación.
 */
public class ControladorInterfazClientes {

    /* Opción de menu para cambiar el estado de la conexión. */
    @FXML private MenuItem menuConexion;
    /* Opción de menu para agregar. */
    @FXML private MenuItem menuAgregar;
    /* Opción de menu para editar. */
    @FXML private MenuItem menuEditar;
    /* Opción de menu para eliminar. */
    @FXML private MenuItem menuEliminar;
    /* Opción de menu para buscar. */
    @FXML private MenuItem menuBuscar;
    /* El botón de agregar. */
    @FXML private Button botonAgregar;
    /* El botón de editar. */
    @FXML private Button botonEditar;
    /* El botón de eliminar. */
    @FXML private Button botonEliminar;
    /* El botón de buscar. */
    @FXML private Button botonBuscar;

    /* La tabla. */
    @FXML private TableView<Cliente> tabla;

    /* La ventana. */
    private Stage escenario;
    /* El modelo de la selección. */
    private TableView.TableViewSelectionModel<Cliente> modeloSeleccion;
    /* La selección. */
    private ObservableList<TablePosition> seleccion;
    /* Los renglones en la tabla. */
    private ObservableList<Cliente> renglones;

    /* La base de datos. */
    private BaseDeDatosClientes bdd;
    /* La conexión del cliente. */
    private Conexion<Cliente> conexion;
    /* Si hay o no conexión. */
    private boolean conectado;

    /* Inicializa el controlador. */
    @FXML private void initialize() {
        renglones = tabla.getItems();
        modeloSeleccion = tabla.getSelectionModel(); 
        modeloSeleccion.setSelectionMode(SelectionMode.MULTIPLE); 
        seleccion = modeloSeleccion.getSelectedCells(); 
        ListChangeListener<TablePosition> lcl = c -> cambioSeleccion(); 
        seleccion.addListener(lcl);
        cambioSeleccion ();

        setConectado(false);
        bdd = new BaseDeDatosClientes();
        bdd.agregaEscucha((e, r, s) -> eventoBaseDeDatos(e, r, s));
    }

    /* Cambioa el estado de la conexión. */
    @FXML private void cambiaConexion(ActionEvent evento) {
        if (!conectado) {
            conectar();
        } else {
            desconectar();
        }
    }

    /**
     * Termina el programa.
     * @param evento el evento que generó la acción.
     */
    @FXML public void salir(Event evento) {
        desconectar();
        Platform.exit();
        System.exit(0);
    }

    /* Agrega un nuevo cliente. */
    @FXML private void agregaCliente(ActionEvent evento) {
        DialogoEditaCliente dialogo;
        try {
            dialogo = new DialogoEditaCliente(escenario , null); 
        } catch (IOException ioe) {
            String mensaje = ("Ocurrió un error al tratar de "+ 
                            "cargar el diálogo de cliente.");
            dialogoError("Error al cargar interfaz", mensaje);
            return; 
        }
        dialogo.showAndWait(); 
        tabla.requestFocus();
        if (!dialogo.isAceptado())
            return; 
        
        modeloSeleccion.clearSelection();
        bdd.agregaRegistro(dialogo.getCliente());
        modeloSeleccion.select(dialogo.getCliente());

        try {
            conexion.enviaMensaje(Mensaje.REGISTRO_AGREGADO);
            conexion.enviaRegistro(dialogo.getCliente());
        } catch (IOException ioe) {
            dialogoError("Error al enviar registro agregado", Mensaje.REGISTRO_AGREGADO.toString());
        }
    }

    /* Edita un cliente. */
    @FXML private void editaCliente(ActionEvent evento) {
        int r = seleccion.get(0).getRow();
        Cliente cliente = renglones.get(r); 
        DialogoEditaCliente dialogo;
        try {
            dialogo = new DialogoEditaCliente(escenario ,
            cliente);
        } catch (IOException ioe) {
            String mensaje = ("Ocurrió un error al tratar de " +
                                "cargar el diálogo de cliente."); 
            dialogoError("Error al cargar interfaz", mensaje);
            return; 
        }
        dialogo.showAndWait(); tabla.requestFocus();
        if (!dialogo.isAceptado())
            return;

        try {
            conexion.enviaMensaje(Mensaje.REGISTRO_MODIFICADO);
            conexion.enviaRegistro(cliente);
            conexion.enviaRegistro(dialogo.getCliente());
        } catch (IOException ioe) {
            dialogoError("Error al enviar registro modificado", Mensaje.REGISTRO_MODIFICADO.toString());
        }

        bdd.modificaRegistro(cliente, dialogo.getCliente());
    }

    /* Elimina uno o varios clientes. */
    @FXML private void eliminaClientes(ActionEvent evento) {
        int s = seleccion.size();
        String titulo = (s > 1) ?
            "Eliminar clientes" : "Eliminar cliente"; 
        String mensaje = (s > 1) ?
            "Esto eliminará a los clientes seleccionados" :
            "Esto eliminará al cliente seleccionado"; String aceptar = titulo;
        String cancelar = (s > 1) ?
            "Conservar clientes" : "Conservar cliente"; 
        if (!dialogoDeConfirmacion(
                titulo , mensaje , "¿Está seguro?", 
                aceptar , cancelar ))
        return;
        for (TablePosition tp : seleccion) {
            bdd.eliminaRegistro(renglones.get(tp.getRow()));

            try {
                conexion.enviaMensaje(Mensaje.REGISTRO_ELIMINADO);
                conexion.enviaRegistro(renglones.get(tp.getRow()));
            } catch (IOException ioe) {
                dialogoError("Error al enviar registro modificado", Mensaje.REGISTRO_MODIFICADO.toString());
                return;
            }
        }
    }

    /* Busca clientes. */
    @FXML private void buscaClientes(ActionEvent evento) {
        DialogoBuscaClientes dialogo;
        try {
            dialogo = new DialogoBuscaClientes(escenario); 
        } catch (IOException ioe) {
            String mensaje = ("Ocurrió un error al tratar de " + 
                                "cargar el diálogo de búsqueda.");
        dialogoError("Error al cargar interfaz", mensaje);
        return; 
        }
        dialogo.showAndWait(); tabla.requestFocus();
        if (!dialogo.isAceptado())
            return;
        Lista <Cliente > resultados =
            bdd.buscaRegistros(dialogo.getCampo(), 
                                dialogo.getValor());
            modeloSeleccion.clearSelection();
        for (Cliente cliente : resultados)
            modeloSeleccion.select(cliente);
    }

    /* Muestra un diálogo con información del programa. */
    @FXML private void acercaDe(ActionEvent evento) {
        Alert dialogo = new Alert(AlertType.INFORMATION); 
        dialogo.initOwner(escenario); 
        dialogo.initModality(Modality.WINDOW_MODAL); 
        dialogo.setTitle("Acerca de Administrador " +
                            "de Clientes"); 
        dialogo.setHeaderText(null);
        dialogo.setContentText("Aplicación para administrar " + 
                                "clientes.\n" +
                                "Copyright © 2022 Facultad " +
                                "de Ciencias, UNAM.");
        dialogo.showAndWait();
        tabla.requestFocus();
    }

    /**
     * Define el escenario.
     * @param escenario el escenario.
     */
    public void setEscenario(Stage escenario) {
        this.escenario = escenario; 
    }

    /* Conecta el cliente con el servidor. */
    private void conectar() {
        DialogoConectar dialogo;
        try {
            dialogo = new DialogoConectar(escenario);
        } catch (IOException ioe) {
            dialogoError("Error al crear diálogo", "");
            return; 
        }

        dialogo.showAndWait(); 
        tabla.requestFocus();


        if (!(dialogo.isAceptado()))
            return;
        String direccion = dialogo.getDireccion();
        int puerto = dialogo.getPuerto();
        try {
            conexion = new Conexion<>(bdd,new Socket(dialogo.getDireccion(),dialogo.getPuerto()));
            new Thread(() -> conexion.recibeMensajes()).start();
            conexion.enviaMensaje(Mensaje.BASE_DE_DATOS);
            conexion.agregaEscucha((c,m) -> mensajeRecibido(c,m));

        } catch (IOException ioe) {
            dialogoError("Error al tratar de conectarse", "");
            return;
        }

        setConectado(true);
    }

    /* Desconecta el cliente del servidor. */
    private void desconectar() {
        if (!this.conectado) {
            return;
        } else {
            conexion.desconecta();
            conexion = null;
            setConectado(false);
            this.bdd.limpia();
        }
    }

    /* Cambia la interfaz gráfica dependiendo si estamos o no conectados. */
    private void setConectado(boolean conectado) {
        if (conectado)
            menuConexion.setText("Desconectar");
        else
            menuConexion.setText("Conectar");
        this.conectado = conectado;
        menuAgregar.setDisable(!conectado);
        botonAgregar.setDisable(!conectado);
        menuBuscar.setDisable(!conectado);
        botonBuscar.setDisable(!conectado);
    }

    /* Maneja un evento de cambio en la base de datos. */
    private void eventoBaseDeDatos(EventoBaseDeDatos evento,
                                   Cliente cliente1,
                                   Cliente cliente2) {
        switch (evento) { 
        case BASE_LIMPIADA:
            Platform.runLater(() -> renglones.clear());
            break;
        case REGISTRO_AGREGADO:
            Platform.runLater(() -> agregaCliente(cliente1));
            break;
        case REGISTRO_ELIMINADO:
            Platform.runLater(() -> eliminaCliente(cliente1));
            break;
        case REGISTRO_MODIFICADO:
            Platform.runLater(() -> tabla.sort());
            break; 
        }
    }

    /* Actualiza la interfaz dependiendo del número de renglones
     * seleccionados. */
    private void cambioSeleccion() {
        int s = seleccion.size(); 
        menuEliminar.setDisable(s == 0); 
        menuEditar.setDisable(s != 1); 
        botonEliminar.setDisable(s == 0); 
        botonEditar.setDisable(s != 1);
    }

    /* Crea un diálogo con una pregunta que hay que confirmar. */
    private boolean dialogoDeConfirmacion(String titulo,
                                          String mensaje, String pregunta,
                                          String aceptar, String cancelar) {
        Alert dialogo = new Alert(AlertType.CONFIRMATION); 
        dialogo.setTitle(titulo); 
        dialogo.setHeaderText(mensaje); 
        dialogo.setContentText(pregunta);
        ButtonType si = new ButtonType(aceptar); 
        ButtonType no = new ButtonType(cancelar , 
                                        ButtonData.CANCEL_CLOSE); 
        dialogo.getButtonTypes().setAll(si, no);
        Optional<ButtonType> resultado = dialogo.showAndWait(); 
        tabla.requestFocus();
        return resultado.get() == si;
    }

    /* Recibe los mensajes de la conexión. */
    private void mensajeRecibido(Conexion<Cliente> conexion, Mensaje mensaje) {
        if (!conexion.isActiva())
    		return;
        switch (mensaje) { 
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
            Platform.runLater(() -> desconectar());
            break;
        case GUARDA:                break; 
        case DETENER_SERVICIO:      break; 
        case ECO:                   break; 
        case INVALIDO:
            Platform.runLater(
                () -> dialogoError("Error con el servidor",
                                    "Mensaje inválido recibido. " +
                                    "Se finalizará la conexión."));
            break; 
        }
    }

    /* Maneja el mensaje BASE_DE_DATOS. */
    private void baseDeDatos(Conexion<Cliente> conexion) {  
        try {
            conexion.recibeBaseDeDatos();
        } catch (IOException ioe) {
            Platform.runLater(
                () -> dialogoError("Error al recibir base de datos","")
            );
        }
    }

    /* Maneja los mensajes REGISTRO_AGREGADO y REGISTRO_MODIFICADO. */
    private void registroAlterado(Conexion<Cliente> conexion,
                                  Mensaje mensaje) { 
        Cliente cliente;
        try {
            cliente = conexion.recibeRegistro();
        } catch (IOException ioe) {
            Platform.runLater(
                () -> dialogoError("Error al recibir registro","")
            );
            return;
        }
        if (mensaje == Mensaje.REGISTRO_AGREGADO)
            bdd.agregaRegistro(cliente);
        else
            bdd.eliminaRegistro(cliente);
    }

    /* Maneja el mensaje REGISTRO_MODIFICADO. */
    private void registroModificado(Conexion<Cliente> conexion) { 
        Cliente c1;
        Cliente c2;
        try {
            c1 = conexion.recibeRegistro();
            c2 = conexion.recibeRegistro();
        } catch (IOException ioe) {
            Platform.runLater(
                () -> dialogoError("Error al recibir registros","")
            );
            return;
        }
        bdd.modificaRegistro(c1,c2);
    }

    /* Muestra un diálogo de error. */
    private void dialogoError(String titulo, String mensaje) {
        if(this.conectado)
            desconectar();

        Alert dialogo = new Alert(AlertType.ERROR); 
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(null); 
        dialogo.setContentText(mensaje);
        dialogo.setOnCloseRequest(e -> renglones.clear());
        dialogo.showAndWait();
        tabla.requestFocus();
    }

    /* Agrega un cliente a la tabla. */
    private void agregaCliente(Cliente cliente) {
        renglones.add(cliente);
        Platform.runLater(() -> tabla.sort());
    }

    /* Elimina un cliente de la tabla. */
    private void eliminaCliente(Cliente cliente) {
        renglones.remove(cliente);
        Platform.runLater(() -> tabla.sort());
    }
}
