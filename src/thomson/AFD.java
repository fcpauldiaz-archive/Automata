package thomson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

/**
* Universidad Del Valle de Guatemala
* 07-ago-2015
* Pablo Díaz 13203
*/

/**
 *
 * @author Pablo
 */
public class AFD<T> {

    private HashSet<String> alfabeto;
    private Estado inicial;
    private ArrayList<Estado> estados;
    private ArrayList<Estado> aceptacion;

    public AFD() {
        this.alfabeto = new HashSet();
        this.estados =  new ArrayList();
        this.aceptacion =  new ArrayList();
    }

    public HashSet<String> getAlfabeto() {
        return alfabeto;
    }

    public void addtAlfabeto(String alfabeto) {
        this.alfabeto.add(alfabeto);
    }

    public Estado getEstadoInicial() {
        return inicial;
    }

    public void setEstadoInicial(Estado inicial) {
        this.inicial = inicial;
    }

    public ArrayList<Estado> getEstados() {
        return estados;
    }

    public void addEstados(Estado estado) {
        this.estados.add(estado);
    }
    public void setEstados(ArrayList<Estado> estados) {
        this.estados = estados;
    }

    public ArrayList<Estado> getEstadosAceptacion() {
        return aceptacion;
    }

    public void addEstadosAceptacion(Estado aceptacion) {
        this.aceptacion.add(aceptacion);
    }
    
    public HashSet<Estado> eClosure(Estado eClosureEstado){
        Stack<Estado> pilaClosure = new Stack();
        Estado actual = eClosureEstado;
        HashSet<Estado> resultado = new HashSet();
        
        pilaClosure.push(actual);
        while(!pilaClosure.isEmpty()){
            actual = pilaClosure.pop();
           
            for (Transicion t: actual.getTransiciones()){
                
                if (t.getSimbolo().equals(AFNThomsonMain.EPSILON)){
                    resultado.add(t.getFin());
                    pilaClosure.push(t.getFin());
                }
            }
        }
        resultado.add(eClosureEstado); //la operacion e-Closure debe tener el estado aplicado
        return resultado;
    }
    public HashSet<Estado> move(HashSet<Estado> estados, Object simbolo){
       
        HashSet<Estado> alcanzados = new HashSet();
        Iterator<Estado> iterador = estados.iterator();
        while (iterador.hasNext()){
            
            for (Transicion t: iterador.next().getTransiciones()){
                Estado siguiente = t.getFin();
                
                Object simb = (String) t.getSimbolo();
                if (simb.equals(simbolo)){
                    alcanzados.add(siguiente);
                  
                   
                }
                
            }
            
        }
        return alcanzados;
    }
}
