package mx.unam.ciencias.icc.igu;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.CampoCliente;

/**
 * Clase para diálogos con formas de búsquedas de clientes.
 */
public class DialogoBuscaClientes extends Stage {

    /* Vista de la forma para realizar búsquedas de clientes. */
    private static final String BUSCA_CLIENTES_FXML =
        "fxml/forma-busca-clientes.fxml";

    /* El controlador. */
    private ControladorFormaBuscaClientes controlador;

    /**
     * Define el estado inicial de un diálogo para búsquedas de clientes.
     * @param escenario el escenario al que el diálogo pertenece.
     * @throws IOException si no se puede cargar el archivo FXML.
     */
    public DialogoBuscaClientes(Stage escenario) throws IOException {
        ClassLoader cl = getClass (). getClassLoader (); 
        FXMLLoader cargador = new FXMLLoader(
            cl.getResource(BUSCA_CLIENTES_FXML)); 
        AnchorPane pagina = (AnchorPane)cargador.load(); 
        setTitle("Buscar clientes"); 
        initOwner(escenario); 
        initModality(Modality.WINDOW_MODAL);
        Scene escena = new Scene(pagina); 
        setScene(escena);
        controlador = cargador.getController(); 
        controlador.setEscenario(this); 
        setOnShown(w -> controlador.defineFoco()); 
        setResizable(false);
    }

    /**
     * Nos dice si el usuario activó el botón de aceptar.
     * @return <code>true</code> si el usuario activó el botón de aceptar,
     *         <code>false</code> en otro caso.
     */
    public boolean isAceptado() {
        return controlador.isAceptado();
    }

    /**
     * Regresa el campo seleccionado.
     * @return el campo seleccionado.
     */
    public CampoCliente getCampo() { 
        return controlador.getCampo();
    }

    /**
     * Regresa el valor ingresado.
     * @return el valor ingresado.
     */
    public Object getValor() {
        return controlador.getValor();
    }
}
