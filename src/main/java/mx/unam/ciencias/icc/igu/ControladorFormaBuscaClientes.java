package mx.unam.ciencias.icc.igu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import mx.unam.ciencias.icc.CampoCliente;

/**
 * Clase para el controlador del contenido del diálogo para buscar clientes.
 */
public class ControladorFormaBuscaClientes
    extends ControladorFormaCliente {

    /* El combo del campo. */
    @FXML private ComboBox<CampoCliente> opcionesCampo;
    /* El campo de texto para el valor. */
    @FXML private EntradaVerificable entradaValor;

    /* Inicializa el estado de la forma. */
    @FXML private void initialize() { 
        entradaValor.setVerificador(v -> verificaValor(v));
        entradaValor.textProperty().addListener( (o, v, n) -> botonAceptar.setDisable(!(entradaValor.esValida())));
    }

    /* Revisa el valor después de un cambio. */
    @FXML private void revisaValor(ActionEvent evento) {  
        Tooltip.install(entradaValor, getTooltip()); 
        botonAceptar.setDisable(!entradaValor.esValida());
    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML private void aceptar(ActionEvent evento) {   
        aceptado = true;
        escenario.close();
    }

    /* Verifica el valor. */
    private boolean verificaValor(String valor) {  
        switch (opcionesCampo.getValue()) {
        case NOMBRE:    return verificaNombre(valor);
        case EDAD:    return verificaEdad(valor) && 
                                    this.edad >= 18 && 
                                    this.edad <= 100;
        case CORREO:  return verificaCorreo(valor);
        case TELEFONO:      return verificaTelefono(valor) && 
                                    this.telefono >= 100000000 && 
                                    this.telefono <= 999999999;
        case GASTO:  return verificaGasto(valor);
        case TRAMITES:      return verificaTramites(valor);
        default:        return false;
        } 
    }

    /* Obtiene la pista. */
    private Tooltip getTooltip() {  
        String m = "";
        switch (opcionesCampo.getValue()) { 
        case NOMBRE:
            m = "Buscar por nombre necesita al menos un carácter";
            break; 
        case EDAD:
            m = "Buscar por edad necesita un número entre " + 
                "18 y 100";
            break; 
        case CORREO:
            m = "Buscar por correo necesita al menos un carácter";
            break; 
        case TELEFONO:
            m = "Buscar por telefono necesita un número entre " + 
                "1000000000 y 9999999999";
            break; 
        case GASTO:
            break; 
        case TRAMITES:
            break; 
        }
        return new Tooltip(m);
    }

    /**
     * Regresa el valor ingresado.
     * @return el valor ingresado.
     */
    public Object getValor() {     
        switch (opcionesCampo.getValue()) { 
        case NOMBRE:
            return entradaValor.getText(); 
        case EDAD:
            return Integer.parseInt(entradaValor.getText()); 
        case CORREO:
            return entradaValor.getText(); 
        case TELEFONO:
            return Integer.parseInt(entradaValor.getText()); 
        case GASTO:
            return Double.parseDouble(entradaValor.getText());
        case TRAMITES: 
            return Integer.parseInt(entradaValor.getText());
        default: 
            return entradaValor.getText();
        }
    }

    /**
     * Regresa el campo seleccionado.
     * @return el campo seleccionado.
     */
    public CampoCliente getCampo() {   
        return opcionesCampo.getValue();
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() { 
        entradaValor.requestFocus();
    }
}
