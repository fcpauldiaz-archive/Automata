/**
* Universidad Del Valle de Guatemala
* 07-ago-2015
* Pablo Díaz 13203
*/

package thomson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

/**
 *
 * @author Pablo
 */
public class Simulacion {
    
    public Simulacion(){
        
    }
    
    public Simulacion(Automata afn_simulacion, String regex){
        simular(afn_simulacion.getEstadoInicial(),regex,afn_simulacion.getEstadosAceptacion());
    }
    
    public HashSet<Estado> eClosure(Estado eClosureEstado){
        Stack<Estado> pilaClosure = new Stack();
        Estado actual = eClosureEstado;
        HashSet<Estado> resultado = new HashSet();
        
        pilaClosure.push(actual);
        while(!pilaClosure.isEmpty()){
            actual = pilaClosure.pop();
           
            for (Transicion t: actual.getTransiciones()){
                
                if (t.getSimbolo().equals(AutomataMain.EPSILON)&&!resultado.contains(t.getFin())){
                    resultado.add(t.getFin());
                    pilaClosure.push(t.getFin());
                }
            }
        }
        resultado.add(eClosureEstado); //la operacion e-Closure debe tener el estado aplicado
        return resultado;
    }
    
    public HashSet<Estado> move(HashSet<Estado> estados, String simbolo){
       
        HashSet<Estado> alcanzados = new HashSet();
        Iterator<Estado> iterador = estados.iterator();
        while (iterador.hasNext()){
            
            for (Transicion t: iterador.next().getTransiciones()){
                Estado siguiente = t.getFin();
                String simb = (String) t.getSimbolo();
                if (simb.equals(simbolo)){
                    alcanzados.add(siguiente);
                }
                
            }
            
        }
        return alcanzados;
        
    }
    
    /**
     * Sobre carga del metodo simular para convertir un hashset a un array.
     * @param inicial
     * @param regex
     * @param aceptacion 
     */
    public void simular(Estado inicial, String regex, HashSet<Estado> aceptacion){
        ArrayList<Estado> array_aceptacion = new ArrayList(aceptacion);
        simular(inicial, regex, array_aceptacion);
    }
    
    public void simular(Estado inicial, String regex, ArrayList<Estado> aceptacion)
    {
        
        HashSet<Estado> conjunto = eClosure(inicial);
        for (Character ch: regex.toCharArray()){
            conjunto = move(conjunto,ch.toString());
            HashSet<Estado> temp = new HashSet();
            Iterator<Estado> iter = conjunto.iterator();
            
            while (iter.hasNext()){
               Estado siguiente = iter.next();
               /**
                * En esta parte es muy importante el metodo addAll
                * porque se tiene que agregar el eClosure de todo el conjunto
                * resultante del move y se utiliza un hashSet temporal porque
                * no se permite la mutacion mientras se itera
                */
                temp.addAll(eClosure(siguiente)); 
               
            }
            conjunto=temp;
            
            
        }
        
        
        boolean resultado = false;
        
        for (Estado estado_aceptacion : aceptacion){
            if (conjunto.contains(estado_aceptacion)){
                resultado = true;
            }
        }
        if (resultado)
            System.out.println("Aceptado");
        else
            System.out.println("NO Aceptado");
    }
    
   

}
