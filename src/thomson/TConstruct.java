/**
* Universidad Del Valle Guatemala
* Pablo Díaz 13203
*/

package thomson;

import java.util.Stack;



/**
 *
 * @author Pablo
 */
public class TConstruct<T> {
    
   
    private AutomataFN afn;
    private final String epsilon = "ε";
    private String regex;
    
     public TConstruct(String regex) {
        RegexConverter convert = new RegexConverter();
        this.regex = convert.infixToPostfix(regex);
    } 
    public void construct(){
        Stack pila = new Stack();
        
        for (Character c : this.regex.toCharArray()) {
            switch(c){
                case '*':
                    Character OP1 = (Character) pila.pop();
                    AutomataFN afn = afnBasico((T) OP1);
                    
                    System.out.println(afn);
                default:
                    pila.push(c);
            }
        }
                
    }
    
    public AutomataFN afnBasico(T simboloRegex)
    {
        AutomataFN automataFN = new AutomataFN();
        Estado inicial = new Estado(0);
        Estado aceptacion = new Estado(1);
        
        Transicion tran = new Transicion(inicial, aceptacion,  simboloRegex);
        inicial.getTransiciones().add(tran);
        
        automataFN.setEstados(inicial);
        automataFN.setEstados(aceptacion);
        
        automataFN.setInicial(inicial);
        automataFN.setFin(aceptacion);
        return automataFN;
       
    }   
    
    public AutomataFN cerraduraKleene(AutomataFN automataFN)
    {
        AutomataFN afn_kleene = new AutomataFN();
                
        // Agregamos el estado inicial
        Estado nuevoInicio = new Estado(0);
        afn_kleene.setEstados(nuevoInicio);
        afn_kleene.setInicial(nuevoInicio);
        
        // Agregamos los demás estados
        for (int i=0; i < automataFN.getEstados().size(); i++) {
            Estado tmp = (Estado) automataFN.getEstados().get(i);
            tmp.setId(i + 1);
            afn_kleene.setEstados(tmp);
        }
        
        // Agregamos el estado final
        Estado nuevoFin = new Estado(automataFN.getEstados().size() + 1);
        afn_kleene.setEstados(nuevoFin);
        afn_kleene.setFin(nuevoFin);
        
        // Estados inicial y final anteriores
        Estado anteriorInicio = automataFN.getInicial();
        Estado anteriorFin    = automataFN.getFin();
        
        // Agregamos transiciones adicionales desde el nuevo estado inicial
        nuevoInicio.getTransiciones().add(new Transicion(nuevoInicio, anteriorInicio, epsilon));
        nuevoInicio.getTransiciones().add(new Transicion(nuevoInicio, nuevoFin, epsilon));
        
        // Agregamos transiciones adicionales desde el anterior estado final
        anteriorFin.getTransiciones().add(new Transicion(anteriorFin, anteriorInicio,epsilon));
        anteriorFin.getTransiciones().add(new Transicion(anteriorFin, nuevoFin, epsilon));
        
        return afn_kleene;
    }

    public AutomataFN getAfn() {
        return afn;
    }

    public void setAfn(AutomataFN afn) {
        this.afn = afn;
    }
    
    
    
    

}
