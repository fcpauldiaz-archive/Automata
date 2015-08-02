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
public class Estado {

    
    private int id;
    private ArrayList<Transicion> transiciones = new ArrayList();

    public Estado(int id, ArrayList<Transicion> transiciones) {
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

    public ArrayList<Transicion> getTransiciones() {
       
        return transiciones;
    }

    public void setTransiciones(Transicion tran) {
        this.transiciones.add(tran);
    }
    
    @Override
    public String toString(){
        return "ID: " + this.id;
    }
    
    
}
