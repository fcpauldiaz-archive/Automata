/**
* Universidad Del Valle Guatemala
* Pablo Díaz 13203
* Descripción: Clase que implementa el algoritmo de Thomson
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
        char[] array = this.regex.toCharArray();
        String mod = new String(array);
        mod = mod.substring(0,1);
         System.out.println(mod);
        this.afn = afnBasico((T) mod);
    } 
    public void construct(){
        Stack pilaAFN = new Stack();
        
        for (Character c : this.regex.toCharArray()) {
            switch(c){
                case '*':
                     AutomataFN kleene = cerraduraKleene((AutomataFN) pilaAFN.pop());
                     pilaAFN.push(kleene);
                     System.out.println(kleene);
                     this.afn=kleene;
                    break;
                case '.':
                    AutomataFN concat_param1 = (AutomataFN)pilaAFN.pop();
                    AutomataFN concat_param2 = (AutomataFN)pilaAFN.pop();
                    AutomataFN concat_result = concatenacion(concat_param1,concat_param2);
                    System.out.println(concat_result);
                    System.out.println("-----");
                    for (Estado e: concat_result.getEstados()){
                        for (Transicion tran : e.getTransiciones()){
                            System.out.println(tran.getFin());
                            System.out.println( tran.getFin().getTransiciones());
                        }
                       
                        
                       
                    }
                    System.out.println("---------");
                    pilaAFN.push(concat_result);
                    //concat_result.simular("abc");
                    this.afn=concat_result;
                    break;
                    
                case '|':
                    
                    AutomataFN union_param1 = (AutomataFN)pilaAFN.pop();
                    AutomataFN union_param2 = (AutomataFN)pilaAFN.pop();
                    AutomataFN union_result = union(union_param1,union_param2);
                    System.out.println(union_result);
                    
                      System.out.println("-----");
                    for (Estado e: union_result.getEstados()){
                        for (Transicion tran : e.getTransiciones()){
                            System.out.println(tran.getFin());
                            System.out.println( tran.getFin().getTransiciones());
                        }
                       
                        
                       
                    }
                    System.out.println("---------");
                    this.afn = union_result;
                    break;
                    
                default:
                   
                    AutomataFN simple = afnBasico((T) c);
                   // System.out.println(simple);
                    pilaAFN.push(simple);
                    //this.afn=simple;
                    
            }
        }
       afn.simular("abasdasd");
                
    }
    
    public AutomataFN afnBasico(T simboloRegex)
    {
        AutomataFN automataFN = new AutomataFN();
        Estado inicial = new Estado(0);
        Estado aceptacion = new Estado(1);
        
        Transicion tran = new Transicion(inicial, aceptacion,  simboloRegex);
        inicial.setTransiciones(tran);
        
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
        
        
        for (int i=0; i < automataFN.getEstados().size(); i++) {
            Estado tmp = (Estado) automataFN.getEstados().get(i);
            tmp.setId(i + 1);
            afn_kleene.addEstados(tmp);
        }
        
        
        Estado nuevoFin = new Estado(automataFN.getEstados().size() + 1);
        afn_kleene.addEstados(nuevoFin);
        afn_kleene.setEstadoFinal(nuevoFin);
        
       
        Estado anteriorInicio = automataFN.getEstadoInicial();
        Estado anteriorFin    = automataFN.getEstadoFinal();
        
        // agregar transiciones adicionales desde el nuevo estado inicial
        nuevoInicio.getTransiciones().add(new Transicion(nuevoInicio, anteriorInicio, epsilon));
        nuevoInicio.getTransiciones().add(new Transicion(nuevoInicio, nuevoFin, epsilon));
        
        // agregar transiciones adicionales desde el anterior estado final
        anteriorFin.getTransiciones().add(new Transicion(anteriorFin, anteriorInicio,epsilon));
        anteriorFin.getTransiciones().add(new Transicion(anteriorFin, nuevoFin, epsilon));
        
        System.out.println(afn_kleene.getEstadoFinal().getId()+"id");
        return afn_kleene;
    }

   public AutomataFN concatenacion(AutomataFN AFN1, AutomataFN AFN2){
       
       AutomataFN afn_concat = new AutomataFN();
            

        int i=0;
        for (i=0; i < AFN2.getEstados().size(); i++) {
            Estado tmp = (Estado) AFN2.getEstados().get(i);
            tmp.setId(i);
            if (i==0){
                afn_concat.setEstadoInicial(tmp);
            }
            if (i == AFN2.getEstados().size()-1){
                tmp.setTransiciones(new Transicion(AFN2.getEstadoFinal(),AFN1.getEstadoInicial(),epsilon));
            }
            afn_concat.addEstados(tmp);

        }
            
        Estado nuevoFin = new Estado(AFN2.getEstados().size()+AFN1.getEstados().size()-1);

        for (int j =0;j<AFN1.getEstados().size();j++){
            Estado tmp = (Estado) AFN1.getEstados().get(j);
            tmp.setId(i);


            if (AFN1.getEstados().size()-1==j)
                afn_concat.setEstadoFinal(tmp);
             afn_concat.addEstados(tmp);
            i++;
        }
        //afn_concat.addEstados(nuevoFin);
        //afn_concat.setEstadoFinal(nuevoFin);
       /* Estado primerFin = automataFN2.getEstadoFinal();
        Estado inicioIntermedio = automataFN1.getEstadoInicial();
        Transicion tran = new Transicion(primerFin,inicioIntermedio,epsilon);
        afn_concat.addEstados(primerFin);
        afn_concat.addEstados(inicioIntermedio);*/
        Estado anteriorInicio = AFN2.getEstadoInicial();
       /*for (int j=0;j<automataFN1.getEstados().size();j++){
           i++;
           Estado tmp = (Estado) automataFN1.getEstados().get(j);
           System.out.println(tmp.getTransiciones());
           tmp.setId(i+1);
           afn_concat.addEstados(tmp);
       }

        */
            
        
       

        Estado anteriorFin    = afn_concat.getEstadoFinal();

       // System.out.println(anteriorFin);
        //afn_concat.addEstados(anteriorFin);*/

        //nuevoInicio.getTransiciones().add(new Transicion(nuevoInicio, anteriorInicio, epsilon));
        System.out.println(anteriorFin);
            
       
       
       return afn_concat;
   }
   
    
    public AutomataFN union(AutomataFN AFN1, AutomataFN AFN2){
        AutomataFN afn_union = new AutomataFN();
        
        Estado nuevoInicio = new Estado(0);
        nuevoInicio.setTransiciones(new Transicion(nuevoInicio,AFN2.getEstadoInicial(),epsilon));

        afn_union.addEstados(nuevoInicio);
        afn_union.setEstadoInicial(nuevoInicio);
        int i=0;
        for (i=0; i < AFN1.getEstados().size(); i++) {
            Estado tmp = (Estado) AFN1.getEstados().get(i);
            tmp.setId(i + 1);
            afn_union.addEstados(tmp);
        }
        
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
        
        // agregar transiciones adicionales desde el nuevo estado inicial
        nuevoInicio.getTransiciones().add(new Transicion(nuevoInicio, anteriorInicio, epsilon));
        
        anteriorFin.getTransiciones().add(new Transicion(anteriorFin, nuevoFin, epsilon));
        anteriorFin2.getTransiciones().add(new Transicion(anteriorFin2,nuevoFin,epsilon));
        
       
        return afn_union;
    }
    
    public AutomataFN getAfn() {
        return afn;
    }

    public void setAfn(AutomataFN afn) {
        this.afn = afn;
    }
    
    
    
    

}
