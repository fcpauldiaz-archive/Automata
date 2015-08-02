/**
* Universidad Del Valle 
* Pablo Díaz 13203
* 02/07/2015
*/

package thomson;

/**
 *
 * @author Pablo
 */
public class Transicion<T> {
    
    //estado inicial de la transicion
    private Estado inicio;
    //estado final de la transiciones
    private Estado fin;
    //simbolo por el cual se realiza la transicion
    private T simbolo;
    
    /**
     * Constructor de una transicion
     * @param inicio
     * @param fin
     * @param simbolo 
     */
    public Transicion(Estado inicio, Estado fin, T simbolo) {
        this.inicio = inicio;
        this.fin = fin;
        this.simbolo = simbolo;
    }
    /**
     * Accesor del estado inicial de la transicion
     * @return Estado
     */
    public Estado getInicio() {
        return inicio;
    }
    /**
     * Mutador del estado inicial de la transicion
     * @param inicio 
     */
    public void setInicio(Estado inicio) {
        this.inicio = inicio;
    }
    
    /**
     * Accesor del estado final de la transiciones
     * @return Estado
     */
    public Estado getFin() {
        return fin;
    }

    /**
     * Mutadro del estado final de la transicion
     * @param fin 
     */
    public void setFin(Estado fin) {
        this.fin = fin;
    }
    /**
     * Obtener el simbolo de la transicion
     * @return String
     */
    public T getSimbolo() {
        return simbolo;
    }

    /**
     * Mutador del simbolo
     * @param simbolo 
     */
    public void setSimbolo(T simbolo) {
        this.simbolo = simbolo;
    }
    /**
     * Mostrar la transicion
     * @return String
     */
    @Override
    public String toString(){
        return "(" + inicio.getId() +"-" + simbolo  +"-"+fin.getId()+")";
    }

}
