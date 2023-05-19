package mx.unam.ciencias.icc;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para listas genéricas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas implementan la interfaz {@link Iterable}, y por lo tanto se
 * pueden recorrer usando la estructura de control <em>for-each</em>. Las listas
 * no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Iterable<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {   
            anterior = null;
            siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if (hasNext() == false)
                throw new NoSuchElementException();
            else {
                Nodo s = siguiente;
                anterior = s;
                siguiente = s.siguiente;
                return s.elemento;
            }
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (hasPrevious() == false)
                throw new NoSuchElementException();
            else {
                Nodo s = anterior;
                siguiente = s;
                anterior = s.anterior;
                return s.elemento;
            }
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
            siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacia() {
        return cabeza == null;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        if (elemento == null) {
            throw new IllegalArgumentException();
        } else {
            Nodo nuevoNodo = new Nodo(elemento);
            longitud++;
            if (esVacia()) {
                cabeza = nuevoNodo;
                rabo = nuevoNodo;
            } else {
                rabo.siguiente = nuevoNodo;
                nuevoNodo.anterior = rabo;
                rabo = nuevoNodo;
            }
        }
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) { 
        if(elemento == null){
            throw new IllegalArgumentException();
        } else {
            Nodo nuevoNodo = new Nodo(elemento);
            longitud++;
            if (esVacia()) {
                cabeza = nuevoNodo;
                rabo = nuevoNodo;
            } else {
                nuevoNodo.siguiente = cabeza;
                cabeza.anterior = nuevoNodo;
                cabeza = nuevoNodo;
            }
        }
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if (elemento == null){
            throw new IllegalArgumentException();
        } else {
            if (i < 1) {
                agregaInicio(elemento);
            } else if (i >= longitud) {
                agregaFinal(elemento);
            } else {
                Nodo nuevoNodo = new Nodo(elemento);

                Nodo aux = getNodo(i);
                Nodo auxAnterior = aux.anterior;
                nuevoNodo.anterior = aux.anterior;
                aux.anterior = nuevoNodo;
                nuevoNodo.siguiente = aux;
                auxAnterior.siguiente = nuevoNodo;
                longitud++;
            }
        }
    }

    public Nodo getNodo(int i) {
        if (i < 0 || i >= longitud) {
            return null;
        } else {
            Nodo aux = cabeza;
            for (int k = 0; k < i ; k++) {
                aux = aux.siguiente;
            }
            return aux;
        }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(T elemento) {
        Nodo nodo = buscaNodo(elemento);
        eliminaNodo(nodo);
    }

    private Nodo buscaNodo(Object elemento) {
        if (elemento == null) {
            return null;
        } else {
            Nodo aux = cabeza;
            while (aux != null) {
                if (aux.elemento.equals(elemento))
                    return aux;
                aux = aux.siguiente;
            } 
            return null;
        }
    }

    private void eliminaNodo(Nodo nodo) {
        if (nodo == null) {
            return;
        } else {
            longitud--;
            if (nodo == cabeza) {
                if (cabeza == rabo) {
                    cabeza = null;
                    rabo = null;
                } else {
                    Nodo aux = nodo.siguiente;

                    aux.anterior = null;
                    cabeza = aux;
                }
            } else if (nodo == rabo){
                Nodo aux = nodo.anterior;

                aux.siguiente = null;
                rabo = aux;
            } else {
                nodo.siguiente.anterior = nodo.anterior;
                nodo.anterior.siguiente = nodo.siguiente;
            }
        }
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if (esVacia()) {
            throw new NoSuchElementException();
        } else {
            T elemento = cabeza.elemento;
            eliminaNodo(cabeza);
            return elemento;
        }
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
         if (esVacia()) {
            throw new NoSuchElementException();
        } else {
            T elemento = rabo.elemento;
            eliminaNodo(rabo);
            return elemento;
        }
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(T elemento) {
        return buscaNodo(elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> lista = new Lista<>();
        Nodo aux = this.cabeza;
        
        while(aux != null) {             
            lista.agregaInicio(aux.elemento);
            aux = aux.siguiente;
        } return lista;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> lista = new Lista<>();
        Nodo aux = cabeza;

        while(aux != null) {             
            lista.agregaFinal(aux.elemento);
            aux = aux.siguiente;
        } return lista;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    public void limpia() {
        cabeza = null;
        rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if (esVacia()) {
            throw new NoSuchElementException(); 
        } else {
            return cabeza.elemento;
        }
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if (esVacia()) {
            throw new NoSuchElementException(); 
        } else {
            return rabo.elemento;
        }
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if (i < 0 || i >= longitud) {
            throw new ExcepcionIndiceInvalido("Índice Inválido");
        } else { 
            if (cabeza == null) {
                return null;
            } else {
                Nodo aux = cabeza;
                for (int k = 0; k < i ; k++) {
                    aux = aux.siguiente;
                } return aux.elemento;
            }
        }
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        if (elemento == null) {
            return -1;
        } else {
            Nodo aux = cabeza;
            for (int i = 0; i < longitud; i++) {
                if (aux.elemento == elemento) {
                    return i;
                } else {
                    aux = aux.siguiente;
                }
            } return -1;
        }
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if (esVacia()) {
            return "[]";
        } else {
            StringBuffer sb = new StringBuffer();
            sb .append("[" + String.format("%s", cabeza.elemento));

            return toString(cabeza.siguiente, sb);  
        }
    }

    public String toString(Nodo nodo, StringBuffer sb) {
        if (nodo == null) {
            sb .append("]");
            return sb.toString();
        } else {
            sb .append(String.format(", %s", nodo.elemento));
            return toString(nodo.siguiente, sb);
        }
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        
        if (!(objeto instanceof Lista))
            return false;

        if (lista == null || longitud != lista.longitud)
            return false;
        Nodo n1 = cabeza;
        Nodo n2 = lista.cabeza;
        while (n1 != null && n2 != null) {
            if (!n1.elemento.equals(n2.elemento))
                return false;
            n1 = n1.siguiente;
            n2 = n2.siguiente;
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        if (longitud < 2) {
            return copia();
        } else {
            Lista<T> l1 = new Lista<>();
            Lista<T> l2 = new Lista<>();
            if (longitud % 2 == 0) {
                Nodo aux = cabeza;
                for (int i = 0; i < (longitud / 2); i++) {
                    l1.agregaFinal(aux.elemento);
                    aux = aux.siguiente;
                }
                for (int j = (longitud / 2); j < longitud; j++) {
                    l2.agregaFinal(aux.elemento);
                    aux = aux.siguiente;
                }
                
            } else {
                Nodo aux = cabeza;
                for (int i = 0; i < ((longitud - 1) / 2); i++) {
                    l1.agregaFinal(aux.elemento);
                    aux = aux.siguiente;
                }
                for (int j = ((longitud - 1) / 2); j < longitud; j++) {
                    l2.agregaFinal(aux.elemento);
                    aux = aux.siguiente;
                }
            }
            l1 = l1.mergeSort(comparador);
            l2 = l2.mergeSort(comparador);

            return mezcla(comparador, l1, l2);
        }
    }

    public Lista<T> mezcla(Comparator<T> comparador, Lista<T> l1, Lista<T> l2) {
        Lista<T> L = new Lista<>();
        Nodo i = l1.cabeza;
        Nodo j = l2.cabeza;

        while((i != null) && (j != null)) {
            if ((comparador.compare(i.elemento, j.elemento)) <= 0) {
                L.agregaFinal(i.elemento);
                i = i.siguiente;
            } else {
                L.agregaFinal(j.elemento);
                j = j.siguiente;
            }
        }

        while (i != null) {
                L.agregaFinal(i.elemento);
                i = i.siguiente;
            }

        while (j != null) {
                L.agregaFinal(j.elemento);
                j = j.siguiente;
            }

        return L;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        if (cabeza == null){
            return false;
        } else {
            Nodo nodo = cabeza;
            while (nodo != null) {
                if (comparador.compare(nodo.elemento, elemento) == 0) {
                    return true;
                } else {
                    nodo = nodo.siguiente;
                }
            }
            return false;
        }
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
