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
 * Clase que construye un AFD a partir de un AFN
 * @author Pablo
 */
public class AFDConstructor {
    
    private Automata afd;
    private final Simulacion simulador;
    
    public AFDConstructor(){
        simulador = new Simulacion();
        afd = new Automata();
    }
    
    
    /**
     * Conversion de un automata AFN a uno AFD por el
     * metodo de subconjuntos
     * @param afn AFN
     */
    public void conversionAFN(Automata afn){
        //se crea una estructura vacia
        Automata automata = new Automata();
        //se utiliza una cola como la estructura para guardar los subconjuntos a analizar
        Queue<HashSet<Estado>> cola = new LinkedList();
        //se crea un nuevo estado inicial
        Estado inicial = new Estado(0);
        automata.setEstadoInicial(inicial);
        automata.addEstados(inicial);
       

        //el algoritmo empieza con el e-Closure del estado inicial del AFN
        HashSet<Estado> array_inicial = simulador.eClosure(afn.getEstadoInicial());
        //si el primer e-closure contiene estados de aceptacion hay que agregarlo
        for (Estado aceptacion:afn.getEstadosAceptacion()){
            if (array_inicial.contains(aceptacion))
                automata.addEstadosAceptacion(inicial);
        }
        
        //lo agregamos a la pila
        cola.add(array_inicial);
        //variable temporal para guardar el resultado todos los subconjuntos creados
        ArrayList<HashSet<Estado>> temporal = new ArrayList();
       //se utilizan esetos indices para saber el estado actuales y anterior
       int indexEstadoInicio = 0;
       while (!cola.isEmpty()){
           //actual subconjunto
            HashSet<Estado> actual = cola.poll();
            //se recorre el subconjunto con cada simbolo del alfabeto del AFN
            for (Object simbolo: afn.getAlfabeto())
            {
                //se realiza el move con el subconjunto
                HashSet<Estado> move_result = simulador.move(actual, (String) simbolo);

                HashSet<Estado> resultado = new HashSet();
                //e-Closure con cada estado del resultado del move y 
                //se guarda en un solo array (merge)
                for (Estado e : move_result) 
                {
                    resultado.addAll(simulador.eClosure(e));
                }

                Estado anterior = (Estado) automata.getEstados().get(indexEstadoInicio);
                /*Si el subconjunto ya fue creado una vez, solo se agregan
                transiciones al automata*/
                if (temporal.contains(resultado))
                {
                    ArrayList<Estado> array_viejo = automata.getEstados();
                    Estado estado_viejo = anterior;
                    //se busca el estado correspondiente y se le suma el offset
                    Estado estado_siguiente = array_viejo.get(temporal.indexOf(resultado)+1);
                    estado_viejo.setTransiciones(new Transicion(estado_viejo,estado_siguiente,simbolo));

                }
                //si el subconjunto no existe, se crea un nuevo estado
                else
                {
                    temporal.add(resultado);
                    cola.add(resultado);

                    Estado nuevo = new Estado(temporal.indexOf(resultado)+1);
                    anterior.setTransiciones(new Transicion(anterior,nuevo,simbolo));
                    automata.addEstados(nuevo);
                    //se verifica si el estado tiene que ser de aceptacion
                    for (Estado aceptacion:afn.getEstadosAceptacion()){
                        if (resultado.contains(aceptacion))
                            automata.addEstadosAceptacion(nuevo);
                    }
                }
               

            }
            indexEstadoInicio++;

           }
        
        this.afd = automata;
        //metodo para definir el alfabeto, se copia el del afn
        definirAlfabeto(afn);
        this.afd.setTipo("AFD");
        System.out.println(afd);
        
    }
    
    public boolean nullable(Nodo expresion){
        //cerradura de kleene siempre retorna verdadero
        if (expresion.getId().equals("*"))
            return true;
        //cuando es or, se verifica cada una las hojas del nodo
        else if (expresion.getId().equals("|"))
            return nullable(expresion.getIzquierda())||nullable(expresion.getDerecha());
        //cuando es concatenacion solo si los dos nodos son true, devuelve true
        else if (expresion.getId().equals("."))
            return nullable(expresion.getDerecha())&&nullable(expresion.getDerecha());
        //si contiene epsilon, es true
        else if (expresion.getId().equals(AutomataMain.EPSILON))
            return true;
        
        //valor por default a regresar
        return false;
        
    }
    
    public ArrayList firstPost(Nodo expresion){
        ArrayList resultado = new ArrayList();
        return resultado;
    }
    
    
    
    private void definirAlfabeto(Automata afn){
        this.afd.setAlfabeto(afn.getAlfabeto());
    }

    public Automata getAfd() {
        return afd;
    }

  
}
