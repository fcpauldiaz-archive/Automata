/**
* Universidad Del Valle 
* Pablo Díaz 13203
* 
*/

package thomson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;


/**
 * Estructura de datos que modela un automata finito no determinista
 * @author Pablo
 */
public class AutomataFN {
    
    //compuesto por un estado inicial
    private Estado inicial;
    //en general deberia ser un arreglo de conjuntos finales
    //pero de acuerdo al algoritmo de thomson, siempre 
    //se mantiene un unico estado de aceptacion/final
    private Estado aceptacion;
    //array de estados
    private ArrayList<Estado> estados = new ArrayList();
    // alfabeto del autómata, hash para no tener elementos repetidos
    private HashSet alfabeto = new HashSet();
    /**
     * Constructor vacio
     */
    public AutomataFN()
    {
        
    }
    
    /**
     * Constructor de un automata con sus estados
     * @param inicial Estado inicial
     * @param aceptacion Estado de aceptacion 
     */
    public AutomataFN(Estado inicial, Estado aceptacion) {
        this.inicial = inicial;
        this.aceptacion = aceptacion;
        
    }
    /**
     * Accesor del estado inicial del autómata
     * @return Estado
     */
    public Estado getEstadoInicial() {
        return inicial;
    }
    /**
     * Mutador del estado inicial del autómata
     * @param inicial Estado inicial
     */
    public void setEstadoInicial(Estado inicial) {
        this.inicial = inicial;
    }
    /**
     * Accesor del estado de aceptacion o final del autómata
     * @return Estado
     */
    public Estado getEstadoFinal() {
        return aceptacion;
    }
    /**
     * Mutador del estado final o aceptacion del autómata
     * @param fin Estado final
     */
    public void setEstadoFinal(Estado fin) {
        this.aceptacion = fin;
    }

    /**
     * Obtener los estados del autómata
     * @return Array de Estados
     */
    public ArrayList<Estado> getEstados() {
        return estados;
    }
    /**
     * Agregar un estado al autómata
     * @param estado estructura de estado
     */
    public void addEstados(Estado estado) {
        this.estados.add(estado);
    }
    /**
     * Simular el autómata de acuerdo a la expresión regular que acepta.
     * @param regex expresion regular (string)
     */
    public void simular(String regex){
        ArrayList letras = new ArrayList();
        boolean control=true;
        Object s = "ε";
          /* Pila para almacenar los estados  */
        Stack<Estado> pila = new Stack<>();
        /* estado para recorrer la pila, empieza con el inicial*/
        Estado actual = inicial;
        /*Arreglo de estados, se utiliza hash para que no se repitan*/
        HashSet<Estado>  alcanzados = new HashSet();
        /*sirve para saber los estados de aceptacion*/
        ArrayList<Estado> finales = new ArrayList();
        Estado alcanzado = inicial;
        //se crean transiciones con el alfabeto para que el automata no acepte
        //expresiones despues de llegar al estado de aceptacion
        for (Object letra: alfabeto){
            aceptacion.getTransiciones().add(new Transicion(aceptacion,new Estado(-1),letra));
        }
        
        /*
         * recorrer caracter por caracter 
         * y avanzar de estado dependiendo de las transiciones
         */
       
        
        
        for (Character ch: regex.toCharArray()){
            if (ch.equals("ε"))
                alcanzados.add(actual);
          
            /* Meter el estado actual como el estado inicial */
            pila.push(actual);
            System.out.println(pila);
            while (!pila.isEmpty()) {
                actual = pila.pop();
//                System.out.println("actual" + actual);
//                System.out.println("trans" + actual.getTransiciones());
                
                //si llega al estado extra, no puede ser aceptado
                if (alcanzado.getId()==-1)
                    alcanzados.remove(actual);
                
                ArrayList<Transicion> transiciones = actual.getTransiciones();
              
                for (Transicion t : transiciones) {
//                    System.out.println(t);
                    Estado e = t.getFin();
                    s = (Object) t.getSimbolo();
//                    System.out.println(e);
//                    System.out.println(s +" sim");

                    if (s.equals(ch)&&!alcanzados.contains(e)) {

                        //System.out.println(pila);
                        if (ch == regex.charAt(regex.length()-1)){
                            finales.add(e);
                          
                        }
                        alcanzados.add(e);
                        alcanzado=e;
                        //System.out.println(alcanzados);
                        pila.push(e);

                    }   
                    if (s.equals(ch))
                        letras.remove(t.getSimbolo());
                    if (!alfabeto.contains(ch))
                        alcanzados.clear();
                    
                    /*
                     * Se busca recursivamente en los estados
                     * si se encuentra la cadena vacia
                     */
                    if (s.equals("ε")){

                        pila.push(e);
                        //System.out.println(pila);

                    }
                    
                }
            }
        }
        
       if (alcanzados.contains(aceptacion))
            System.out.println("Aceptado");
       else
            System.out.println("No Aceptado");
    }

    /**
     * Mostrar los atributos del autómata
     * @return String
     */
    public HashSet getAlfabeto() {
        return alfabeto;
    }
    
    /**
     * Metodo para definir el alfabeto del automata a partir 
     * de la expresion regular
     * @param regex 
     */
    public void setAlfabeto(String regex) {
        for (Character ch: regex.toCharArray()){
           
            System.out.println();
            if (ch != '|' && ch != '.' && ch != '*' && ch != AFNThomsonMain.EPSILON_CHAR)
                this.alfabeto.add(ch);
        }
    }

    
    @Override
    public String toString(){
        String res = new String();
        res += "Alfabeto " + this.alfabeto+"\n";
        res += "Estado inicial " + this.inicial +"\n";
        res += "Estado de aceptacion " + this.aceptacion +"\n";
        res += "Conjunto de Estados " + this.estados.toString()+"\n";
        res += "Conjunto de transiciones ";
        for (int i =0 ; i<this.estados.size();i++){
             Estado est = estados.get(i);
             res += est.getTransiciones()+"-";
        }
        
        
        return res;
    }

  
    

}
