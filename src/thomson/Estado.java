/**
* Universidad Del Valle 
* Pablo Díaz 13203
*/

package thomson;

import java.util.ArrayList;

/**
 *
 * @author Pablo
 */
public class Estado<T> {

    
    private int id;
    private ArrayList<T> transiciones;

    public Estado(int id, ArrayList<T> transiciones) {
        this.id = id;
        this.transiciones = transiciones;
    }

    public Estado(int identificador) {
        this.id = identificador;
        
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<T> getTransiciones() {
        return transiciones;
    }

    public void setTransiciones(ArrayList<T> transiciones) {
        this.transiciones = transiciones;
    }
    
    
    
    
}
