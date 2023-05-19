package mx.unam.ciencias.icc;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Clase para representar clientes. Un cliente tiene nombre, edad,
 * correo, teléfono, gasto y trámites realizados. La clase implementa {@link Registro}, por lo que
 * puede seriarse en una línea de texto y deseriarse de una línea de
 * texto; además de determinar si sus campos casan valores arbitrarios y
 * actualizarse con los valores de otro cliente.
 */
public class Cliente implements Registro<Cliente, CampoCliente> {

    /* Nombre del cliente. */
    private final StringProperty nombre;
    /* Edad del cliente. */
    private final IntegerProperty edad;
    /* Correo del cliente. */
    private final StringProperty correo;
    /* Teléfono del cliente. */
    private final IntegerProperty telefono;
    /* Gasto del cliente. */
    private final DoubleProperty gasto;
    /* Número de trámites realizados por del cliente.*/
    private final IntegerProperty tramites;

    /**
     * Define el estado inicial de un cliente.
     * @param nombre el nombre del cliente.
     * @param edad la edad del cliente.
     * @param correo el correo del cliente.
     * @param telefono el número telefónico del cliente.
     * @param gasto el gasto del cliente.
     * @param tramites el número de trámites realizados por el cliente.
     */
    public Cliente(String nombre,
                      int    edad,
                      String correo,
                      int    telefono,
                      double gasto,
                      int    tramites) { 
        this.nombre     = new SimpleStringProperty(nombre);                    
        this.edad       = new SimpleIntegerProperty(edad); 
        this.correo     = new SimpleStringProperty(correo); 
        this.telefono   = new SimpleIntegerProperty(telefono);                    
        this.gasto      = new SimpleDoubleProperty(gasto);
        this.tramites   = new SimpleIntegerProperty(tramites);
    }

    /**
     * Regresa el nombre del cliente.
     * @return el nombre del cliente.
     */
    public String getNombre() {
        return nombre.get();
    }

    /**
     * Define el nombre del cliente.
     * @param nombre el nuevo nombre del cliente.
     */
    public void setNombre(String nombre) {
        this.nombre.set(nombre);    
    }

    /**
     * Regresa la propiedad del nombre.
     * @return la propiedad del nombre.
     */
    public StringProperty nombreProperty() {
        return nombre;
    }

    /**
     * Regresa la edad del cliente.
     * @return la edad del cliente.
     */
    public int getEdad() {
        return edad.get();
    }

    /**
     * Define la edad del cliente.
     * @param edad la nueva edad del cliente.
     */
    public void setEdad(int edad) {
        this.edad.set(edad);
    }

    /**
     * Regresa la propiedad de la edad.
     * @return la propiedad de la edad.
     */
    public IntegerProperty edadProperty() {
        return edad;
    }

    /**
     * Regresa el correo del cliente.
     * @return el correo del cliente.
     */
    public String getCorreo() {
        return correo.get();
    }

    /**
     * Define el correo del cliente.
     * @param correo el nuevo correo del cliente.
     */
    public void setCorreo(String correo) {
        this.correo.set(correo);    
    }

    /**
     * Regresa la propiedad del correo.
     * @return la propiedad del correo.
     */
    public StringProperty correoProperty() {
        return correo;
    }

    /**
     * Regresa el teléfono del cliente.
     * @return el teléfono del cliente.
     */
    public int getTelefono() { 
        return telefono.get();
    }

    /**
     * Define el teléfono del cliente.
     * @param telefono el nuevo teléfono del cliente.
     */
    public void setTelefono(int telefono) {
        this.telefono.set(telefono);
    }

    /**
     * Regresa la propiedad del teléfono.
     * @return la propiedad del teléfono.
     */
    public IntegerProperty telefonoProperty() {
        return telefono;
    }

    /**
     * Regresa el gasto del cliente.
     * @return el gasto del cliente.
     */
    public double getGasto() {
        return gasto.get();
    }

    /**
     * Define el gasto del cliente.
     * @param gasto el nuevo gasto del cliente.
     */
    public void setGasto(double gasto) {
        this.gasto.set(gasto);
    }

    /**
     * Regresa la propiedad del gasto.
     * @return la propiedad del gasto.
     */
    public DoubleProperty gastoProperty() {
        return gasto;
    }

    /**
     * Regresa el número de trámites del cliente.
     * @return el número de trámites del cliente.
     */
    public int getTramites() {
        return tramites.get();
    }

    /**
     * Define el número de trámites del cliente.
     * @param tramites el nuevo número de trámites del cliente.
     */
    public void setTramites(int tramites) {
        this.tramites.set(tramites);
    }

    /**
     * Regresa la propiedad de los trámites.
     * @return la propiedad de los trámites.
     */
    public IntegerProperty tramitesProperty() {
        return tramites;
    }

    /**
     * Regresa una representación en cadena del cliente.
     * @return una representación en cadena del cliente.
     */
    @Override public String toString() {
        return String.format("Nombre   : %s\n" +
                             "Edad     : %d\n" +
                             "Correo   : %s\n" +
                             "Teléfono : %10d\n" +
                             "Gasto    : %2.2f\n" +
                             "Trámites : %d",
                             nombre.get() , edad.get() , correo.get() , telefono.get(),
                             gasto.get(), tramites.get());
    }

    /**
     * Nos dice si el objeto recibido es un cliente igual al que manda llamar
     * el método.
     * @param objeto el objeto con el que el cliente se comparará.
     * @return <code>true</code> si el objeto recibido es un cliente con las
     *         mismas propiedades que el objeto que manda llamar al método,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {  
        if (!(objeto instanceof Cliente))
            return false;
        Cliente cliente = (Cliente)objeto;
        if (!(objeto instanceof Cliente))
            return false;
        if(cliente == null)
            return false;
        else
            return nombre.get().equals(cliente.getNombre()) &&
                   edad.get() == cliente.getEdad() &&
                   correo.get().equals(cliente.getCorreo()) &&
                   telefono.get() == cliente.getTelefono() &&
                   gasto.get() == cliente.getGasto() &&
                   tramites.get() == cliente.getTramites();
    }

    /**
     * Regresa el cliente seriado en una línea de texto. La línea de
     * texto que este método regresa debe ser aceptada por el método {@link
     * Cliente#deseria}.
     * @return la seriación del cliente en una línea de texto.
     */
    @Override public String seria() {
        return String.format("%s\t%d\t%s\t%d\t%2.2f\t%d\n",
                                     nombre.get() , edad.get() , correo.get() , telefono.get(),
                                     gasto.get(), tramites.get());
    }

    /**
     * Deseria una línea de texto en las propiedades del cliente. La
     * seriación producida por el método {@link Cliente#seria} debe
     * ser aceptada por este método.
     * @param linea la línea a deseriar.
     * @throws ExcepcionLineaInvalida si la línea recibida es nula, vacía o no
     *         es una seriación válida de un cliente.
     */
    @Override public void deseria(String linea) {
        if (linea == null){
            throw new ExcepcionLineaInvalida();
        } else {
            String trim = linea.trim();
            while (trim != trim.trim()){
                trim = trim.trim();
            }     
            if (trim == ""){
                throw new ExcepcionLineaInvalida();
            } else {
                if (trim.split("\t").length != 6) {
                    throw new ExcepcionLineaInvalida();
                } else {
                    setNombre(trim.split("\t")[0]);
                    try {
                        setEdad(Integer.parseInt(trim.split("\t")[1]));
                    } catch (Exception e) {
                        throw new ExcepcionLineaInvalida();
                    } 
                    setCorreo(trim.split("\t")[2]);
                    try {
                        setTelefono(Integer.parseInt(trim.split("\t")[3]));
                    } catch (Exception e) {
                        throw new ExcepcionLineaInvalida();
                    } 
                    try {
                        setGasto(Double.parseDouble(trim.split("\t")[4]));
                    } catch (Exception e) {
                        throw new ExcepcionLineaInvalida();
                    }
                    try {
                        setTramites(Integer.parseInt(trim.split("\t")[5]));
                    } catch (Exception e) {
                        throw new ExcepcionLineaInvalida();
                    }          
                }
            }
        }
    }

    /**
     * Actualiza los valores del cliente con los del cliente recibido.
     * @param cliente el cliente con el cual actualizar los valores.
     * @throws IllegalArgumentException si el cliente es <code>null</code>.
     */
    public void actualiza(Cliente cliente) {
        if (cliente != null) {
            if (cliente instanceof Cliente){
                Cliente c = cliente;
                setNombre(c.getNombre());
                setEdad(c.getEdad());
                setCorreo(c.getCorreo());
                setTelefono(c.getTelefono());
                setGasto(c.getGasto());
                setTramites(c.getTramites());
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Nos dice si el cliente casa el valor dado en el campo especificado.
     * @param campo el campo que hay que casar.
     * @param valor el valor con el que debe casar el campo del registro.
     * @return <code>true</code> si:
     *         <ul>
     *           <li><code>campo</code> es {@link CampoCliente#NOMBRE} y
     *              <code>valor</code> es instancia de {@link String} y es una
     *              subcadena del nombre del cliente.</li>
     *           <li><code>campo</code> es {@link CampoCliente#EDAD} y
     *              <code>valor</code> es instancia de {@link Integer} y su
     *              valor entero es menor o igual a la edad del
     *              cliente.</li>
     *           <li><code>campo</code> es {@link CampoCliente#CORREO} y
     *              <code>valor</code> es instancia de {@link String} y es una
     *              subcadena del correo del cliente.</li>
     *           <li><code>campo</code> es {@link CampoCliente#TELEFONO} y
     *              <code>valor</code> es instancia de {@link Integer} y su
     *              valor entero es menor o igual al teléfono del
     *              cliente.</li>
     *           <li><code>campo</code> es {@link CampoCliente#GASTO} y
     *              <code>valor</code> es instancia de {@link Double} y su
     *              valor doble es menor o igual al gasto del
     *              cliente.</li>
     *           <li><code>campo</code> es {@link CampoCliente#TRAMITES} y
     *              <code>valor</code> es instancia de {@link Integer} y su
     *              valor entero es menor o igual a los trámites del
     *              cliente.</li>
     *         </ul>
     *         <code>false</code> en otro caso.
     * @throws IllegalArgumentException si el campo es <code>null</code>.
     */
    @Override public boolean casa(CampoCliente campo, Object valor) {
        if (!(campo instanceof CampoCliente)) {
            throw new IllegalArgumentException();
        } else {
            switch((CampoCliente)campo){
                case NOMBRE: 
                    if (!(valor instanceof String) || valor == null || valor == "") {
                        return false;
                    } else {
                        if (this.getNombre().contains((String)valor))
                         return true;
                    }
                case EDAD: 
                    if (!(valor instanceof Integer)) {
                        return false;
                    } else {
                        if (this.getEdad() >= ((Integer)valor)) 
                            return true;
                    }
                case CORREO: 
                    if (!(valor instanceof String) || valor == null || valor == "") {
                        return false;
                    } else {
                        if (this.getCorreo().contains((String)valor))
                         return true;
                    }
                case TELEFONO: 
                    if (!(valor instanceof Integer)) {
                        return false;
                    } else {
                        if (this.getTelefono() >= ((Integer)valor)) 
                            return true;
                    }
                case GASTO: 
                    if (!(valor instanceof Double)) {
                        return false;
                    } else {
                        if (this.getGasto() >= ((Double)valor)) 
                            return true;
                    }
                case TRAMITES: 
                    if (!(valor instanceof Integer)) {
                        return false;
                    } else {
                        if (this.getTramites() >= ((Integer)valor)) 
                            return true;
                    }
                default: return false;
            }
        }
    }
}
