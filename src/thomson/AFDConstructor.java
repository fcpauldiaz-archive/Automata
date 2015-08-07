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
    private Simulacion simulador;
    
    public AFDConstructor(){
        simulador = new Simulacion();
    }
    
    
    public void convertAFN(AFN afn){
        AFD automata = new AFD();
        Estado inicial = new Estado(0);
        automata.setEstadoInicial(inicial);
        automata.addEstados(inicial);
        
        
        HashSet<Estado> hashset = simulador.eClosure(afn.getEstadoInicial());
        System.out.println("eclosure inicial");
        System.out.println(hashset);
        HashSet<Estado> LOL = simulador.move(hashset, "a");
        
        ArrayList<Estado> resultado = new ArrayList();
        for (Estado e : LOL) {
            resultado.addAll(simulador.eClosure(e));
        }
        Estado nuevo = new Estado(99);
        nuevo.setTransiciones(new Transicion(inicial,nuevo,"a"));
       
     
        
        
        
        System.out.println("LOL");
        System.out.println(resultado);
        
        
        
        
        
    }

}
