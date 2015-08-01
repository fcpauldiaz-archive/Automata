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
public class AutomataFN<T> {
    
    private Estado inicial;
    private Estado fin;
    private String regex;
    private ArrayList<T> estados = new ArrayList();
    
    
    public AutomataFN()
    {
        
    }
    
    
    public AutomataFN(Estado inicial, Estado fin, String regex) {
        this.inicial = inicial;
        this.fin = fin;
        this.regex = regex;
    }
    
    public AutomataFN(Estado inicial, Estado fin) {
        this.inicial = inicial;
        this.fin = fin;
        
    }

    
    
    public Estado getInicial() {
        return inicial;
    }

    public void setInicial(Estado inicial) {
        this.inicial = inicial;
    }

    public Estado getFin() {
        return fin;
    }

    public void setFin(Estado fin) {
        this.fin = fin;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public ArrayList<T> getEstados() {
        return estados;
    }

    public void setEstados(T estado) {
        this.estados.add(estado);
    }
    
    @Override
    public String toString(){
        String res = new String();
        res += "Estado inicial " + inicial +"\n";
        res += "Estado de aceptacion " + fin +"\n";
        res += "Conjunto de Estados " + estados.toString()+"\n";
       
        return res;
    }
    

}
