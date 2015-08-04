/**
* Universidad Del Valle Guatemala
* Pablo Díaz 13203
* Descripción: Clase que implementa el algoritmo de Thomson
*/

package thomson;

import java.util.HashSet;
import java.util.Stack;

/**
 * Clase que implementa el algoritmo de Thomson
 * @author Pablo
 */
public class TConstruct<T> {
    
   
    private AutomataFN afn;
    private final String regex;
   
    
    public TConstruct(String regex) {
        RegexConverter converter = new RegexConverter();
        this.regex = converter.infixToPostfix(regex);
       
        
    }
    
   
    /**
     * Metodo que construye el autómata
     */
    public void construct(){
        Stack pilaAFN = new Stack();
        
        for (Character c : this.regex.toCharArray()) {
            switch(c){
                case '*':
                     AutomataFN kleene = cerraduraKleene((AutomataFN) pilaAFN.pop());
                     pilaAFN.push(kleene);
                     this.afn=kleene;
                    break;
                case '.':
                    AutomataFN concat_param1 = (AutomataFN)pilaAFN.pop();
                    AutomataFN concat_param2 = (AutomataFN)pilaAFN.pop();
                    AutomataFN concat_result = concatenacion(concat_param1,concat_param2);
                    System.out.println("-----");
                    for (Estado e: concat_result.getEstados()){
                        for (Transicion tran : e.getTransiciones()){
                            System.out.println(tran.getFin());
                            System.out.println( tran.getFin().getTransiciones());
                        }
                       
                        
                       
                    }
                    System.out.println("---------");
                    pilaAFN.push(concat_result);
                    this.afn=concat_result;
                    break;
                    
                case '|':
                    
                    AutomataFN union_param1 = (AutomataFN)pilaAFN.pop();
                    AutomataFN union_param2 = (AutomataFN)pilaAFN.pop();
                    AutomataFN union_result = union(union_param1,union_param2);
                   
                    System.out.println("-----");
                    for (Estado e: union_result.getEstados()){
                        for (Transicion tran : e.getTransiciones()){
                            System.out.println(tran.getFin());
                            System.out.println( tran.getFin().getTransiciones());
                        }
                       
                        
                       
                    }
                    pilaAFN.push(union_result);
                    System.out.println("---------");
                    this.afn = union_result;
                    break;
                    
                default:
                    //crear un automata con cada simbolo
                    AutomataFN simple = afnSimple((T) c);
                    pilaAFN.push(simple);
                    
                   
                    
            }
        }
        this.afn.setAlfabeto(regex);
        System.out.println(this.afn);
        //this.afn.simular("abb");
        FileCreator crearArchivo = new FileCreator(this.afn.toString());
       
                
    }
    
    public AutomataFN afnSimple(T simboloRegex)
    {
        AutomataFN automataFN = new AutomataFN();
        //definir los nuevos estados
        Estado inicial = new Estado(0);
        Estado aceptacion = new Estado(1);
        //crear una transicion unica con el simbolo
        Transicion tran = new Transicion(inicial, aceptacion,  simboloRegex);
        inicial.setTransiciones(tran);
        //agrega los estados creados
        automataFN.addEstados(inicial);
        automataFN.addEstados(aceptacion);
        
        automataFN.setEstadoInicial(inicial);
        automataFN.setEstadoFinal(aceptacion);
        return automataFN;
       
    }   
    
    public AutomataFN cerraduraKleene(AutomataFN automataFN)
    {
        AutomataFN afn_kleene = new AutomataFN();
                
        
        Estado nuevoInicio = new Estado(0);
        afn_kleene.addEstados(nuevoInicio);
        afn_kleene.setEstadoInicial(nuevoInicio);
        
        //agregar todos los estados intermedio
        for (int i=0; i < automataFN.getEstados().size(); i++) {
            Estado tmp = (Estado) automataFN.getEstados().get(i);
            tmp.setId(i + 1);
            afn_kleene.addEstados(tmp);
        }
        
        
        Estado nuevoFin = new Estado(automataFN.getEstados().size() + 1);
        afn_kleene.addEstados(nuevoFin);
        afn_kleene.setEstadoFinal(nuevoFin);
        
        //definir estados clave para realizar la cerraduras
        Estado anteriorInicio = automataFN.getEstadoInicial();
        Estado anteriorFin    = automataFN.getEstadoFinal();
        
        // agregar transiciones desde el nuevo estado inicial
        nuevoInicio.getTransiciones().add(new Transicion(nuevoInicio, anteriorInicio, AFNThomsonMain.EPSILON));
        nuevoInicio.getTransiciones().add(new Transicion(nuevoInicio, nuevoFin, AFNThomsonMain.EPSILON));
        
        // agregar transiciones desde el anterior estado final
        anteriorFin.getTransiciones().add(new Transicion(anteriorFin, anteriorInicio,AFNThomsonMain.EPSILON));
        anteriorFin.getTransiciones().add(new Transicion(anteriorFin, nuevoFin, AFNThomsonMain.EPSILON));
        
        return afn_kleene;
    }

   public AutomataFN concatenacion(AutomataFN AFN1, AutomataFN AFN2){
       
       AutomataFN afn_concat = new AutomataFN();
            

        int i=0;
        //agregar los estados del primer automata
        for (i=0; i < AFN2.getEstados().size(); i++) {
            Estado tmp = (Estado) AFN2.getEstados().get(i);
            tmp.setId(i);
            //se define el estado inicial
            if (i==0){
                afn_concat.setEstadoInicial(tmp);
            }
            //cuando llega al penultimo, concatena el ultimo con el primero del otro automata con un epsilon
            if (i == AFN2.getEstados().size()-1){
                tmp.setTransiciones(new Transicion(AFN2.getEstadoFinal(),AFN1.getEstadoInicial(),AFNThomsonMain.EPSILON));
            }
            afn_concat.addEstados(tmp);

        }
        //termina de agregar los estados y transiciones del segundo automata
        for (int j =0;j<AFN1.getEstados().size();j++){
            Estado tmp = (Estado) AFN1.getEstados().get(j);
            tmp.setId(i);

            //define el ultimo con estado de aceptacion
            if (AFN1.getEstados().size()-1==j)
                afn_concat.setEstadoFinal(tmp);
             afn_concat.addEstados(tmp);
            i++;
        }
       
       return afn_concat;
   }
   
    
    public AutomataFN union(AutomataFN AFN1, AutomataFN AFN2){
        AutomataFN afn_union = new AutomataFN();
        
        Estado nuevoInicio = new Estado(0);
        nuevoInicio.setTransiciones(new Transicion(nuevoInicio,AFN2.getEstadoInicial(),AFNThomsonMain.EPSILON));

        afn_union.addEstados(nuevoInicio);
        afn_union.setEstadoInicial(nuevoInicio);
        int i=0;//llevar el contador del identificador de estados
        //agregar los estados del segundo automata
        for (i=0; i < AFN1.getEstados().size(); i++) {
            Estado tmp = (Estado) AFN1.getEstados().get(i);
            tmp.setId(i + 1);
            afn_union.addEstados(tmp);
        }
        //agregar los estados del primer automata
        for (int j=0; j < AFN2.getEstados().size(); j++) {
            Estado tmp = (Estado) AFN2.getEstados().get(j);
            tmp.setId(i + 1);
            afn_union.addEstados(tmp);
            i++;
        }
        
        
        Estado nuevoFin = new Estado(AFN1.getEstados().size() +AFN2.getEstados().size()+ 1);
        afn_union.addEstados(nuevoFin);
        afn_union.setEstadoFinal(nuevoFin);
        
       
        Estado anteriorInicio = AFN1.getEstadoInicial();
        Estado anteriorFin    = AFN1.getEstadoFinal();
        Estado anteriorFin2    = AFN2.getEstadoFinal();
        
        // agregar transiciones desde el nuevo estado inicial
        nuevoInicio.getTransiciones().add(new Transicion(nuevoInicio, anteriorInicio, AFNThomsonMain.EPSILON));
        
        anteriorFin.getTransiciones().add(new Transicion(anteriorFin, nuevoFin, AFNThomsonMain.EPSILON));
        anteriorFin2.getTransiciones().add(new Transicion(anteriorFin2,nuevoFin,AFNThomsonMain.EPSILON));
        
       
        return afn_union;
    }
    
    public AutomataFN getAfn() {
        return afn;
    }

    public void setAfn(AutomataFN afn) {
        this.afn = afn;
    }
    
    
    
    

}
