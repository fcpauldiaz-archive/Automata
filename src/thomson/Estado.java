/**
* Universidad Del Valle 
* Pablo Díaz 13203
*
*/

package thomson;

import java.util.ArrayList;

/**
 * Clase para simular un estado de un autómata
 * @author Pablo
 */
public class Estado {

    //atributos
    
    //identificador del estado
    private int id;  
    //transiciones del estado
    private ArrayList<Transicion> transiciones = new ArrayList();

    /**
     *  Constructor
     * @param id identificador
     * @param transiciones transiciones
     */
    public Estado(int id, ArrayList<Transicion> transiciones) {
        this.id = id;
        this.transiciones = transiciones;
    }
    /**
     * Constructor de un estado con solo el identificador
     * @param identificador identificador
     */
    public Estado(int identificador) {
        this.id = identificador;
        
    }
    
    /**
     * Accesor del atributo identificador
     * @return id
     */
    public int getId() {
        return id;
    }
    /**
     * Mutador del atributo identificador
     * @param id identificador
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Accesor del atributo transiciones
     * @return array de transiciones
     */
    public ArrayList<Transicion> getTransiciones() {
       
        return transiciones;
    }
    /**
     * Agregar transiciones al estado
     * @param tran transicion
     */
    public void setTransiciones(Transicion tran) {
        this.transiciones.add(tran);
    }
    /**
     * Mostrar el estado
     * @return String
     */
    @Override
    public String toString(){
        return "ID: " + this.id;
    }
    
    
}
