package mx.unam.ciencias.icc.igu;

/**
 * Clase abstracta para controladores del contenido de diálogo con formas con
 * datos de clientes que se aceptan o rechazan.
 */
public abstract class ControladorFormaCliente extends ControladorForma {

    /** El valor del nombre. */
    protected String nombre;
    /** El valor de la edad. */
    protected int edad;
    /** El valor del correo. */
    protected String correo;
    /** El valor del número de teléfono. */
    protected int telefono;
    /** El valor del gasto. */
    protected double gasto;
    /** El valor de los trámites. */
    protected int tramites;

    /**
     * Verifica que el nombre sea válido.
     * @param nombre el nombre a verificar.
     * @return <code>true</code> si el nombre es válido; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaNombre(String nombre) {   
        if (nombre == null || nombre.isEmpty())
            return false; 
        this.nombre = nombre; 
        return true;
    }

    /**
     * Verifica que la edad sea válida.
     * @param edad la edad a verificar.
     * @return <code>true</code> si la edad es válida; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaEdad(String edad) {    
        if (edad == null || edad.isEmpty())
            return false; 
        try {
            this.edad = Integer.parseInt(edad); 
        } catch (NumberFormatException nfe) {
            return false; 
        }
        return true;
    }

    /**
     * Verifica que el correo sea válido.
     * @param nombre el correo a verificar.
     * @return <code>true</code> si el correo es válido; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaCorreo(String correo) { 
        if (correo == null || correo.isEmpty())
            return false; 
        this.correo = correo; 
        return true;
    }

    /**
     * Verifica que el número telefónico sea válido.
     * @param telefono el número telefónico a verificar.
     * @return <code>true</code> si el número telefónico es válido;
     *         <code>false</code> en otro caso.
     */
    protected boolean verificaTelefono(String telefono) {
        if (telefono == null || telefono.isEmpty())
            return false; 
        try {
            this.telefono = Integer.parseInt(telefono); 
        } catch (NumberFormatException nfe) {
            return false; 
        }
        return true;
    }

    /**
     * Verifica que el gasto sea válido.
     * @param promedio el gasto a verificar.
     * @return <code>true</code> si el gasto es válido; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaGasto(String gasto) {
        if (gasto == null || gasto.isEmpty())
            return false; 
        try {
            this.gasto = Double.parseDouble(gasto); 
        } catch (NumberFormatException nfe) {
            return false; 
        }
        return true;
    }

    /**
     * Verifica que el número de trámites sea válido.
     * @param telefono el número de trámites a verificar.
     * @return <code>true</code> si el número de trámites es válido;
     *         <code>false</code> en otro caso.
     */
    protected boolean verificaTramites(String tramites) {
        if (tramites == null || tramites.isEmpty())
            return false; 
        try {
            this.tramites = Integer.parseInt(tramites); 
        } catch (NumberFormatException nfe) {
            return false; 
        }
        return true;
    }
}
