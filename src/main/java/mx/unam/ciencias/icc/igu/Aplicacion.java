package mx.unam.ciencias.icc.igu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Clase para clientes con interfaz gráfica del servidor de la base de datos.
 */
public class Aplicacion extends Application {

    /* Vista de la interfaz clientes. */
    private static final String INTERFAZ_CLIENTES_FXML =
        "fxml/interfaz-clientes.fxml";
    /* Ícono de la Facultad de Ciencias. */
    private static final String ICONO =
        "icons/favicon_blanco.png";

    /**
     * Inicia la aplicación.
     * @param escenario la ventana principal de la aplicación.
     * @throws Exception si algo sale mal.
     */
    @Override public void start(Stage escenario) throws Exception {     
        ClassLoader cl = getClass (). getClassLoader ();
        String url = cl.getResource(ICONO).toString(); 
        escenario.getIcons().add(new Image(url)); 
        escenario.setTitle("Administrador de Clientes");
        FXMLLoader cargador; cargador = new FXMLLoader(
                                        cl.getResource(INTERFAZ_CLIENTES_FXML)); 
        BorderPane interfazClientes = (BorderPane)cargador.load(); 
        ControladorInterfazClientes controlador = cargador.getController(); 
        controlador.setEscenario(escenario);
        Scene escena = new Scene(interfazClientes); 
        escenario.setScene(escena); 
        escenario.setOnCloseRequest(e -> controlador.salir(e)); 
        escenario.show();
    }
}
