/**
* Universidad Del Valle Guatemala
* Pablo Díaz 13203
*/

package thomson;



/**
 *
 * @author Pablo
 */
public class TConstructor {
    
   
    private AutomataFN afn;
    
    public TConstructor(String simboloRegex)
    {
        AutomataFN automataFN = new AutomataFN();
        Estado inicial = new Estado(0);
        Estado aceptacion = new Estado(1);
        
        Transicion tran = new Transicion(inicial, aceptacion, simboloRegex);
        inicial.getTransiciones().add(tran);
        
        automataFN.setEstados(inicial);
        automataFN.setEstados(aceptacion);
        
        automataFN.setInicial(inicial);
        automataFN.setFin(aceptacion);
        this.afn = automataFN;
       
    }   
    
    public AutomataFN cerraduraKleene(AutomataFN automataFN)
    {
        AutomataFN afn_salida = new AutomataFN();
                
        // Agregamos el estado inicial
        Estado nuevoInicio = new Estado(0);
        afn_salida.setEstados(nuevoInicio);
        afn_salida.setInicial(nuevoInicio);
        
        // Agregamos los demás estados
        for (int i=0; i < automataFN.getEstados().size(); i++) {
            Estado tmp = (Estado) automataFN.getEstados().get(i);
            tmp.setId(i + 1);
            afn_salida.setEstados(tmp);
        }
        
        // Agregamos el estado final
        Estado nuevoFin = new Estado(automataFN.getEstados().size() + 1);
        afn_salida.setEstados(nuevoFin);
        afn_salida.setFin(nuevoFin);
        
        // Estados inicial y final anteriores
        Estado anteriorInicio = automataFN.getInicial();
        Estado anteriorFin    = automataFN.getFin();
        
        // Agregamos transiciones adicionales desde el nuevo estado inicial
        nuevoInicio.getTransiciones().add(new Transicion(nuevoInicio, anteriorInicio, "€"));
        nuevoInicio.getTransiciones().add(new Transicion(nuevoInicio, nuevoFin, "€"));
        
        // Agregamos transiciones adicionales desde el anterior estado final
        anteriorFin.getTransiciones().add(new Transicion(anteriorFin, anteriorInicio,"€"));
        anteriorFin.getTransiciones().add(new Transicion(anteriorFin, nuevoFin, "€"));
        
        return afn_salida;
    }

    public AutomataFN getAfn() {
        return afn;
    }

    public void setAfn(AutomataFN afn) {
        this.afn = afn;
    }
    
    
    
    

}
