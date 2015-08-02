/**
* Universidad Del Valle 
* Pablo Díaz 13203
*/

package thomson;

/**
 *
 * @author Pablo
 */
public class Transicion<T> {
    
    private Estado inicio;
    private Estado fin;
    private T simbolo;

    public Transicion(Estado inicio, Estado fin, T simbolo) {
        this.inicio = inicio;
        this.fin = fin;
        this.simbolo = simbolo;
    }

    public Estado getInicio() {
        return inicio;
    }

    public void setInicio(Estado inicio) {
        this.inicio = inicio;
    }

    public Estado getFin() {
        return fin;
    }

    public void setFin(Estado fin) {
        this.fin = fin;
    }

    public T getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(T simbolo) {
        this.simbolo = simbolo;
    }
    
    @Override
    public String toString(){
        return "(" + inicio.getId() +"-" + simbolo  +"-"+fin.getId()+")";
    }

}
