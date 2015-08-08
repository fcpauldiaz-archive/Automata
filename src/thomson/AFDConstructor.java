/**
* Universidad Del Valle de Guatemala
* 07-ago-2015
* Pablo Díaz 13203
*/

package thomson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author Pablo
 */
public class AFDConstructor {
    
    private AFD afd;
    private final Simulacion simulador;
    
    public AFDConstructor(){
        simulador = new Simulacion();
    }
    
    
    public void convertAFN(AFN afn){
        AFD automata = new AFD();
        Estado inicial = new Estado(0);
        automata.setEstadoInicial(inicial);
        automata.addEstados(inicial);
        boolean marcado = false;
        
        ArrayList<ArrayList<Estado>> temporal = new ArrayList();
        
        
       int index2=0;
        int index =1;
        for (Estado estadosAFN : afn.getEstados()){
            HashSet<Estado> closureEstados = simulador.eClosure(estadosAFN);
            System.out.println("Closure");
            System.out.println(closureEstados);
           
            for (Object simbolo: afn.getAlfabeto()){
                HashSet<Estado> LOL = simulador.move(closureEstados, (String) simbolo);
                System.out.println(LOL);
                ArrayList<Estado> resultado = new ArrayList();
                for (Estado e : LOL) {
                    resultado.addAll(simulador.eClosure(e));
                }
                System.out.println(resultado);
                Estado anterior = (Estado) automata.getEstados().get(index2);
                
                System.out.println(temporal.contains(resultado));
                temporal.add(resultado);
                System.out.println("array de array");
                System.out.println(temporal);
                
                
                Estado nuevo = new Estado(temporal.indexOf(resultado)+1);
                anterior.setTransiciones(new Transicion(anterior,nuevo,simbolo));
                automata.addEstados(nuevo);
                System.out.println(automata);
                
              
              index++;
            }
            index2++;
           
            
        }
        
       
       
     
        
        
        
        System.out.println("LOL");
       
        
        
        
        
        
    }
    
    public void conversion(AFN afn){
        AFD automata = new AFD();
        Queue<HashSet<Estado>> pila = new LinkedList();
        Estado inicial = new Estado(0);
        automata.setEstadoInicial(inicial);
        automata.addEstados(inicial);
        boolean marcado = false;
        HashSet<Estado> array_inicial = simulador.eClosure(afn.getEstadoInicial());
        
        
        pila.add(array_inicial);
        ArrayList<HashSet<Estado>> temporal = new ArrayList();
       
       int index2=0;
       int index =1;
       while (!pila.isEmpty()){
            HashSet<Estado> actual = pila.poll();
            
           
            System.out.println(actual);
            for (Object simbolo: afn.getAlfabeto()){
                    HashSet<Estado> LOL = simulador.move(actual, (String) simbolo);
                    System.out.println(LOL);
                    HashSet<Estado> resultado = new HashSet();
                    for (Estado e : LOL) {
                        resultado.addAll(simulador.eClosure(e));
                    }
                    System.out.println(resultado);
                    Estado anterior = (Estado) automata.getEstados().get(index2);
                   
                    System.out.println(temporal.contains(resultado));
                    

                    if (temporal.contains(resultado)){
                        ArrayList<Estado> array_viejo = automata.getEstados();
                        Estado estado_viejo = anterior;
                        Estado estado_siguiente = array_viejo.get(temporal.indexOf(resultado)+1);
                        estado_viejo.setTransiciones(new Transicion(estado_viejo,estado_siguiente,simbolo));
                        
                    }
                    
                    else{
                    temporal.add(resultado);
                    pila.add(resultado);
                    System.out.println("array de array");
                    System.out.println(temporal);
                    Estado nuevo = new Estado(temporal.indexOf(resultado)+1);
                    anterior.setTransiciones(new Transicion(anterior,nuevo,simbolo));
                    automata.addEstados(nuevo);
                    
                    }
                    System.out.println(automata);
                  index++;
                }
                index2++;

           }
        
    }

}
