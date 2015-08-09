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

/**
 *
 * @author Pablo
 */
public class AFDConstructor {
    
    private AFD afd;
    private final Simulacion simulador;
    
    public AFDConstructor(){
        simulador = new Simulacion();
        afd = new AFD();
    }
    
    
    /**
     * Conversion de un automata AFN a uno AFD por el
     * metodo de subconjuntos
     * @param afn AFN
     */
    public void conversionAFN(AFN afn){
       
        //se crea una estructura vacia
        AFD automata = new AFD();
        
        Queue<HashSet<Estado>> pila = new LinkedList();
        Estado inicial = new Estado(0);
        automata.setEstadoInicial(inicial);
        automata.addEstados(inicial);
        
        HashSet<Estado> array_inicial = simulador.eClosure(afn.getEstadoInicial());
        
        
        pila.add(array_inicial);
        ArrayList<HashSet<Estado>> temporal = new ArrayList();
       
       int index2=0;
       int index =1;
       while (!pila.isEmpty()){
            HashSet<Estado> actual = pila.poll();
           
           
            for (Object simbolo: afn.getAlfabeto()){
                    HashSet<Estado> LOL = simulador.move(actual, (String) simbolo);
                    
                    HashSet<Estado> resultado = new HashSet();
                    for (Estado e : LOL) {
                        resultado.addAll(simulador.eClosure(e));
                    }
                   
                    Estado anterior = (Estado) automata.getEstados().get(index2);
                   
                   
                    

                    if (temporal.contains(resultado)){
                        ArrayList<Estado> array_viejo = automata.getEstados();
                        Estado estado_viejo = anterior;
                        Estado estado_siguiente = array_viejo.get(temporal.indexOf(resultado)+1);
                        estado_viejo.setTransiciones(new Transicion(estado_viejo,estado_siguiente,simbolo));
                        
                    }
                    
                    else{
                        temporal.add(resultado);
                        pila.add(resultado);
                     
                        Estado nuevo = new Estado(temporal.indexOf(resultado)+1);
                        anterior.setTransiciones(new Transicion(anterior,nuevo,simbolo));
                        automata.addEstados(nuevo);

                        for (Estado aceptacion:afn.getEstadoFinal()){
                            if (resultado.contains(aceptacion))
                                automata.addEstadosAceptacion(nuevo);
                        }
                    }
                  
                  index++;
                }
                index2++;

           }
        
       this.afd = automata;
        //metodo para definir el alfabeto, se copia el del afn
        definirAlfabeto(afn);
        System.out.println(afd);
        
    }
    
    private void definirAlfabeto(AFN afn){
        this.afd.setAlfabeto(afn.getAlfabeto());
    }

    public AFD getAfd() {
        return afd;
    }

  
}
