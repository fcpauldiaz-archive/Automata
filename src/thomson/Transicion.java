/**
* Universidad Del Valle 
* Pablo Díaz 13203
*/

package thomson;

/**
 *
 * @author Pablo
 */
public class Transicion {
    
    private Estado inicio;
    private Estado fin;
    private String simbolo;

    public Transicion(Estado inicio, Estado fin, String simbolo) {
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

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    
    

}
