/**
* Universidad Del Valle de Guatemala
* 09-ago-2015
* Pablo Díaz 13203
*/

package thomson;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Clase para modelar un automata finito determinista o no determnista
 * @author Pablo
 */
public class Automata {
    
      //compuesto por un estado inicial
    private Estado inicial;
    //en general deberia ser un arreglo de conjuntos finales
    //pero de acuerdo al algoritmo de thomson, siempre 
    //se mantiene un unico estado de aceptacion/final
    private ArrayList<Estado> aceptacion = new ArrayList();
    //array de estados
    private ArrayList<Estado> estados = new ArrayList();
    // alfabeto del autómata, hash para no tener elementos repetidos
    private HashSet alfabeto = new HashSet();
    //atributo para saber si es Determinista o No determinista
    private String tipo;
     /**
     * Constructor vacio
     */
    public Automata()
    {
        
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
    public ArrayList<Estado> getEstadosAceptacion() {
        return aceptacion;
    }
    /**
     * Mutador del estado final o aceptacion del autómata
     * @param fin Estado final
     */
    public void addEstadosAceptacion(Estado fin) {
        this.aceptacion.add(fin);
    }

    /**
     * Obtener los estados del autómata
     * @return Array de Estados
     */
    public ArrayList<Estado> getEstados() {
        return estados;
    }
   
    public Estado getEstados(int index){
        return estados.get(index);
    }
    
    /**
     * Agregar un estado al autómata
     * @param estado estructura de estado
     */
    public void addEstados(Estado estado) {
        this.estados.add(estado);
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
    public void createAlfabeto(String regex) {
        for (Character ch: regex.toCharArray()){
           
            
            if (ch != '|' && ch != '.' && ch != '*' && ch != AutomataMain.EPSILON_CHAR)
                this.alfabeto.add(Character.toString(ch));
        }
    }
    
    public void setAlfabeto(HashSet alfabeto){
        this.alfabeto=alfabeto;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    
    public String getTipo(){
        return this.tipo;
    }

    @Override
    public String toString(){
        String res = new String();
        res += "-------"+this.tipo+"---------\r\n";
        res += "Alfabeto " + this.alfabeto+"\r\n";
        res += "Estado inicial " + this.inicial +"\r\n";
        res += "Conjutos de estados de aceptacion " + this.aceptacion +"\r\n";
        res += "Conjunto de Estados " + this.estados.toString()+"\r\n";
        res += "Conjunto de transiciones ";
        for (int i =0 ; i<this.estados.size();i++){
             Estado est = estados.get(i);
             res += est.getTransiciones()+"-";
        }
        
        
        return res;
    }
    /*
    public void generarDOT(String nombreArchivo){
        String texto = "digraph finite_state_machine {\n";

        texto +="\trankdir=LR;"+"\n";
        int numero = 8;
        numero +=(int)(this.estados.size()/(10));
        texto +="\tsize=\""+numero+",5\""+"\n";
        texto +="\tnode [shape=doublecircle];";
        //listar estados de aceptación...
        for(int i=0;i<this.aceptacion.size();i++){
            texto+=" "+aceptacion.get(i);
        }
        //
        texto+=";"+"\n";
        texto +="\tnode [shape=circle];"+"\n";
	//transiciones
        for(int i=0;i<this.estados.size();i++){
            for (int j = 0;j<this.estados.get(i).getTransiciones().size();j++)
            texto+="\t"+estados.get(i).getTransiciones().get(j).DOT_String()+"\n";
        }
        texto+="}";

        String archivo = "/Automatas/";
        boolean booleano = new File(archivo).mkdir();
        archivo+=nombreArchivo+".dot";

        File TextFile = new File(archivo);
        FileWriter TextOut;
        try {
            TextOut = new FileWriter(TextFile.getAbsoluteFile());
            TextOut.write(texto);
            TextOut.close();
        } catch (Exception ex) {

        }
        System.out.println("TRY");
        //ejecutar comando: dot -Tjpg -O digraph2.dot
        String comando = "dot -Tjpg -O "+archivo;
        try
        {
           /* directorio/ejecutable es el path del ejecutable y un nombre 
           Process p = Runtime.getRuntime().exec (comando);
        }
        catch (Exception e)
        {
           /* Se lanza una excepción si no se encuentra en ejecutable o el fichero no es ejecutable. 
        }
        //this.Print(comando);

    }
*/
}
