/**
* Universidad Del Valle de Guatemala
* 07-ago-2015
* Pablo Díaz 13203
*/

package thomson;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Pablo
 */
public class AFDConstructor {
    
    private AFD afd;
    
    
    public void convertAFN(AFN afn){
        AFD automata = new AFD();
        Estado inicial = new Estado(0);
        automata.setEstadoInicial(inicial);
        automata.addEstados(inicial);
        
        
        
        HashSet<Estado> hashset = afd.eClosure(afn.getEstadoInicial());
        System.out.println("eclosure inicial");
        System.out.println(hashset);
        Object simbolo = "a";
        HashSet<Estado> LOL = this.afd.move(hashset, simbolo);
        
        ArrayList<Estado> resultado = new ArrayList();
        for (Estado e : LOL) {
            resultado.addAll(this.afd.eClosure(e));
        }
        Estado nuevo = new Estado(99);
        nuevo.setTransiciones(new Transicion(inicial,nuevo,"a"));
       
     
        
        
        
        System.out.println("LOL");
        System.out.println(resultado);
        
        
        
        
        
    }

}
