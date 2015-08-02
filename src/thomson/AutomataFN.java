/**
* Universidad Del Valle 
* Pablo Díaz 13203
*/

package thomson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;


/**
 *
 * @author Pablo
 */
public class AutomataFN {
    
    private Estado inicial;
    private Estado fin;
    private ArrayList<Estado> estados = new ArrayList();
    
    
    public AutomataFN()
    {
        
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

   
    public ArrayList<Estado> getEstados() {
        return estados;
    }

    public void setEstados(Estado estado) {
        this.estados.add(estado);
    }
    
    public void simular(String regex){
        
        
        Object s = "ε";
          /* Pila para almacenar los estados pendientes */
        Stack<Estado> pila = new Stack<>();
        Estado actual = inicial;
        HashSet<Estado>  alcanzados = new HashSet();
        ArrayList<Estado> finales = new ArrayList();
        int iteraciones = 0;
        boolean compare = true;
        /*
         * Cuando el símbolo buscado es igual al símbolo
         * vacío, el estado desde donde se empieza el 
         * recorrido debe incluirse entre los estados
         * alcanzados.
         */
        char[] charArray = regex.toCharArray();
        for (Character ch: charArray){
        if (ch.equals("ε"))
            alcanzados.add(actual);
      
        /* Meter el estado actual como el estado inicial */
        pila.push(actual);
        System.out.println(pila);
        while (!pila.isEmpty()) {
            actual = pila.pop();
            System.out.println("actual" + actual);
            System.out.println("trans" + actual.getTransiciones());
           if (!s.equals(ch)){
                alcanzados.remove(actual);
           }
            for (Transicion t : actual.getTransiciones()) {
                System.out.println(t);
                Estado e = t.getFin();
                s = (Object) t.getSimbolo();
                System.out.println(e);
                System.out.println(s +" sim");
                
                
                if (s.equals(ch)&&!alcanzados.contains(e)) {
                   
                    System.out.println(pila);
                    alcanzados.add(e);
                    System.out.println(alcanzados);
                    pila.push(e);
        
                } 
                
                   /*
                     * Debido a que sólo cuando el símbolo buscado
                     * es igual a vacío se debe recorrer recursivamente
                     * los estados alcanzados, agregamos dichos estados
                     * a la pila solo si se da esa condición.
                     */
                     System.out.println(s);
                     
                    if (s.equals("ε")){
                        
                        pila.push(e);
                        System.out.println(pila);
                      
                    }
                  
                   
                
            }
             
        }
        iteraciones++;
        }
        System.out.println("alcanzados"+alcanzados);
        Iterator<Estado> itr = alcanzados.iterator();
        while(itr.hasNext()){
            Estado e = itr.next();
            pila.push(e);
        }
        while (!pila.isEmpty()) {
            actual = pila.pop();
            for (Transicion t : actual.getTransiciones()) {
                System.out.println(t);
                Estado e = t.getFin();
                s = (Object) t.getSimbolo();
                System.out.println(s +" sim");
                
                
                   
               
                   /*
                     * Debido a que sólo cuando el símbolo buscado
                     * es igual a vacío se debe recorrer recursivamente
                     * los estados alcanzados, agregamos dichos estados
                     * a la pila solo si se da esa condición.
                     */
                     System.out.println(s);
                     
                    if (s.equals("ε")){
                        
                        pila.push(e);
                         System.out.println(pila);
                         alcanzados.add(e);
                    }
                  
                   
                
            }
             
        }
        System.out.println(alcanzados);
         
        Iterator<Estado> itr2 = alcanzados.iterator();
        while(itr2.hasNext()){
            if (fin == itr2.next())
                System.out.println("Aceptado");
        }

       
    }
    
    
    @Override
    public String toString(){
        String res = new String();
        res += "Estado inicial " + inicial +"\n";
        res += "Estado de aceptacion " + fin +"\n";
        res += "Conjunto de Estados " + this.estados.toString()+"\n";
        res += "Conjunto de transiciones ";
        for (int i =0 ; i<this.estados.size();i++){
             Estado est = estados.get(i);
             res += est.getTransiciones()+"-";
        }
        
       
        return res;
    }
    

}
