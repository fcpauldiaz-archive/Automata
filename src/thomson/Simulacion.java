/**
* Universidad Del Valle de Guatemala
* 07-ago-2015
* Pablo Díaz 13203
*/

package thomson;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

/**
 * Clase para utilizar el metodo de move, e-closure y simulacion de
 * un automata
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
        actual.getTransiciones();
        HashSet<Estado> resultado = new HashSet();
        
        pilaClosure.push(actual);
        while(!pilaClosure.isEmpty()){
            actual = pilaClosure.pop();
           
            for (Transicion t: (ArrayList<Transicion>)actual.getTransiciones()){
                
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
            
            for (Transicion t: (ArrayList<Transicion>)iterador.next().getTransiciones()){
                Estado siguiente = t.getFin();
                String simb = (String) t.getSimbolo();
                if (simb.equals(simbolo)){
                    alcanzados.add(siguiente);
                }
                
            }
            
        }
        return alcanzados;
        
    }
    
    public Estado move(Estado estado, String simbolo){
        ArrayList<Estado> alcanzados = new ArrayList();
           
        for (Transicion t: (ArrayList<Transicion>)estado.getTransiciones()){
            Estado siguiente = t.getFin();
            String simb = (String) t.getSimbolo();
            
            if (simb.equals(simbolo)&&!alcanzados.contains(siguiente)){
                alcanzados.add(siguiente);
            }

        }
       
        return alcanzados.get(0);
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
        /**
         * Método para crear el archivo DOT para después generar PNG
         * @param nombreArchivo
         * @param automataFinito
         * @return String con el comando a ejecutar.
         * Es necesario tener la librería de Graphiz instalada para correr
         * el comando
         * DOT: Graph description language
         * http://www.graphviz.org/
         * https://en.wikipedia.org/wiki/DOT_(graph_description_language)
         * http://rich-iannone.github.io/DiagrammeR/graphviz.html
         */
     public String generarDOT(String nombreArchivo,Automata automataFinito){
        String texto = "digraph automata_finito {\n";

        texto +="\trankdir=LR;"+"\n";
        int numero = 8;
        numero +=(int)(automataFinito.getEstados().size()/(10));
        texto +="\tsize=\""+numero+",5\""+"\n";
        texto +="\tnode [shape=doublecircle, style = filled,color = mediumseagreen];";
        //listar estados de aceptación
        for(int i=0;i<automataFinito.getEstadosAceptacion().size();i++){
            texto+=" "+automataFinito.getEstadosAceptacion().get(i);
        }
        //
        texto+=";"+"\n";
        texto +="\tnode [shape=circle];"+"\n";
        texto +="\tnode [color=cornflowerblue];\n" +"	edge [color=red]";
	//transiciones
        for(int i=0;i<automataFinito.getEstados().size();i++){
            for (int j = 0;j<automataFinito.getEstados().get(j).getTransiciones().size();j++){
                ArrayList<Transicion> t =automataFinito.getEstadosAceptacion().get(j).getTransiciones();
                Transicion t2 = t.get(i);
                texto+="\t"+t2.DOT_String()+"\n";
            }
           
        }
        texto+="}";
       
        
        
       
        File dummy = new File("");
        String path = dummy.getAbsolutePath();
        path+="/";
        String archivo =nombreArchivo+".dot";
        new File(path+"/GeneracionAutomatas/").mkdirs();
        path+="GeneracionAutomatas/";
        File TextFile = new File("/GeneracionAutomatas/"+nombreArchivo+".dot");
        FileWriter TextOut;
        System.out.println(path);
        System.out.println(TextFile.getAbsoluteFile());
        try {
            TextOut = new FileWriter(path+nombreArchivo+".dot");
            TextOut.write(texto);
            System.out.println("EXITO");
            TextOut.close();
        } catch (Exception ex) {
            System.out.println("ERROR");
        }
        
        
        String comando = "dot -Tpng "+path+archivo + " > "+path+nombreArchivo+".png";
        try
        {
           /* directorio/ejecutable es el path del ejecutable y un nombre */
        //   Process p = Runtime.getRuntime().exec(comando);
        }
        catch (Exception e)
        {
            e.printStackTrace();
           /* Se lanza una excepción si no se encuentra en ejecutable o el fichero no es ejecutable. */
        }
        System.out.println(comando);
        
        return comando;
    }
   

}
