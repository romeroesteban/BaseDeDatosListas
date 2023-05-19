package mx.unam.ciencias.icc.igu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mx.unam.ciencias.icc.Cliente;

/**
 * Clase para el controlador del contenido del diálogo para editar y crear
 * clientes.
 */
public class ControladorFormaEditaCliente
    extends ControladorFormaCliente {

    /* La entrada verificable para el nombre. */
    @FXML private EntradaVerificable entradaNombre;
    /* La entrada verificable para la edad. */
    @FXML private EntradaVerificable entradaEdad;
    /* La entrada verificable para el correo. */
    @FXML private EntradaVerificable entradaCorreo;
    /* La entrada verificable para el teléfono. */
    @FXML private EntradaVerificable entradaTelefono;
    /* La entrada verificable para el gasto. */
    @FXML private EntradaVerificable entradaGasto;
    /* La entrada verificable para los trámites. */
    @FXML private EntradaVerificable entradaTramites;

    /* El cliente creado o editado. */
    private Cliente cliente;

    /* Inicializa el estado de la forma. */
    @FXML private void initialize() {
        entradaNombre.setVerificador(n -> verificaNombre(n)); 
        entradaEdad.setVerificador(c -> verificaEdad(c)); 
        entradaCorreo.setVerificador(p->verificaCorreo(p)); 
        entradaTelefono.setVerificador(e -> verificaTelefono(e));
        entradaGasto.setVerificador(m->verificaGasto(m)); 
        entradaTramites.setVerificador(q -> verificaTramites(q));
        entradaNombre.textProperty().addListener( (o, v, n) -> verificaCliente ());
        entradaEdad.textProperty().addListener( (o, v, n) -> verificaCliente ());
        entradaCorreo.textProperty().addListener( (o, v, n) -> verificaCliente ()); 
        entradaTelefono.textProperty().addListener((o, v, n) -> verificaCliente ());
        entradaGasto.textProperty().addListener( (o, v, n) -> verificaCliente ()); 
        entradaTramites.textProperty().addListener((o, v, n) -> verificaCliente ());
    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML private void aceptar(ActionEvent evento) {
        actualizaCliente ();
        aceptado = true;
        escenario.close();
    }

    /* Actualiza al cliente, o lo crea si no existe. */
    private void actualizaCliente() {
        if (cliente != null) {
            cliente.setNombre(nombre); 
            cliente.setEdad(edad); 
            cliente.setCorreo(correo); 
            cliente.setTelefono(telefono);
            cliente.setGasto(gasto); 
            cliente.setTramites(tramites);
        } else {
            cliente = new Cliente(nombre, edad, correo, 
                                        telefono, gasto, tramites);
        }
    }

    /**
     * Define el cliente del diálogo.
     * @param cliente el nuevo cliente del diálogo.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente == null)
            return;
        this.cliente = new Cliente(null, 0, null, 0, 0, 0); 
        this.cliente.actualiza(cliente); 
        entradaNombre.setText(cliente.getNombre()); 
        String e = String.valueOf(cliente.getEdad()); 
        entradaEdad.setText(e);
        entradaCorreo.setText(cliente.getCorreo()); 
        String t = String.valueOf(cliente.getTelefono()); 
        entradaTelefono.setText(t);
        String g = String.format("%2.2f", cliente.getGasto()); 
        entradaGasto.setText(g);
        String n = String.valueOf(cliente.getTramites()); 
        entradaTramites.setText(n);
    }

    /**
     * Regresa el cliente del diálogo.
     * @return el cliente del diálogo.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Define el verbo del botón de aceptar.
     * @param verbo el nuevo verbo del botón de aceptar.
     */
    public void setVerbo(String verbo) { 
        botonAceptar.setText(verbo);
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        entradaNombre.requestFocus();
    }

    /* Verifica que los seis campos sean válidos. */
    private void verificaCliente() {
        boolean n = entradaNombre.esValida();
        boolean e = entradaEdad.esValida();
        boolean c = entradaCorreo.esValida(); 
        boolean t = entradaTelefono.esValida(); 
        boolean g = entradaGasto.esValida(); 
        boolean nt = entradaTramites.esValida(); 
        botonAceptar.setDisable(!n || !e || !c || !t || !g || !nt);
    }

    /**
     * Verifica que la edad sea válida.
     * @param edad la edad a verificar.
     * @return <code>true</code> si la edad es válida; <code>false</code> en
     *         otro caso.
     */
    @Override protected boolean verificaEdad(String edad) {
        return super.verificaEdad(edad) &&
            this.edad >= 18 && this.edad <= 100;
    }

    /**
     * Verifica que el correo sea válido.
     * @param correo el correo a verificar.
     * @return <code>true</code> si el correo es válido;
     *         <code>false</code> en otro caso.
     */
    @Override protected boolean verificaCorreo(String correo) {
        return super.verificaCorreo(correo) &&
            // this.cuenta >= 10000000 && 
            this.correo.contains("@");
    }

    /**
     * Verifica que el teléfono sea válido.
     * @param telefono el teléfono a verificar.
     * @return <code>true</code> si el teléfono es válido; <code>false</code> en
     *         otro caso.
     */
    @Override protected boolean verificaTelefono(String telefono) {
        return super.verificaTelefono(telefono) &&
            this.telefono >= 100000000 && this.telefono <= 999999999;
    }
}
