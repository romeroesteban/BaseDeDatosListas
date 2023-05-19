package mx.unam.ciencias.icc;

/**
 * Enumeración para los campos de un {@link Cliente}.
 */
public enum CampoCliente {

    /** El nombre del cliente. */
    NOMBRE,
    /** La edad del cliente. */
    EDAD,
    /** El correo del cliente. */
    CORREO,
    /** El número de teléfono del cliente. */
    TELEFONO,
    /** El gasto del cliente. */
    GASTO,
    /** El número de trámites realizados por el cliente. */
    TRAMITES;

    /**
     * Regresa una representación en cadena del campo para ser usada en
     * interfaces gráficas.
     * @return una representación en cadena del campo.
     */
    @Override public String toString() {
        switch (this) {
            case NOMBRE: return "Nombre";
            case EDAD: return "Edad";
            case CORREO: return "Correo";
            case TELEFONO: return "Teléfono";
            case GASTO: return "Gasto";
            case TRAMITES: return "Trámites";
            default: return ""; 
        }
    }
}
