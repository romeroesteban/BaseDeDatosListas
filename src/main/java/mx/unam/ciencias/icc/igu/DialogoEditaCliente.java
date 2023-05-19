package mx.unam.ciencias.icc.igu;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.Cliente;

/**
 * Clase para diálogos con formas para editar clientes.
 */
public class DialogoEditaCliente extends Stage {

    /* Vista de la forma para agregar/editar clientes. */
    private static final String EDITA_CLIENTE_FXML =
        "fxml/forma-edita-cliente.fxml";

    /* El controlador. */
    private ControladorFormaEditaCliente controlador;

    /**
     * Define el estado inicial de un diálogo para cliente.
     * @param escenario el escenario al que el diálogo pertenece.
     * @param cliente el cliente; puede ser <code>null</code> para agregar
     *                   un cliente.
     * @throws IOException si no se puede cargar el archivo FXML.
     */
    public DialogoEditaCliente(Stage escenario,
                                  Cliente cliente) throws IOException {
        ClassLoader cl = getClass (). getClassLoader (); 
        FXMLLoader cargador = new FXMLLoader(cl.getResource(EDITA_CLIENTE_FXML)); 
        AnchorPane pagina = (AnchorPane)cargador.load(); 
        initOwner(escenario); 
        initModality(Modality.WINDOW_MODAL);
        Scene escena = new Scene(pagina); 
        setScene(escena);
        controlador = cargador.getController(); 
        controlador.setEscenario(this); 
        controlador.setCliente(cliente); 
        if (cliente == null) {
            setTitle("Agregar cliente");
            controlador.setVerbo("Agregar"); 
        } else {
            setTitle("Editar cliente");
            controlador.setVerbo("Actualizar"); 
        }
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
     * Regresa el cliente del diálogo.
     * @return el cliente del diálogo.
     */
    public Cliente getCliente() {
        return controlador.getCliente();
    }
}
