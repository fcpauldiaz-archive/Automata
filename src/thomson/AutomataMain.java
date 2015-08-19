/**
* Universidad del Valle de Guatemala
* Pablo Diaz 13203
* 2/07/2015
* a través del algoritmo de Thomson
*/

package thomson;

import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Proyecto para construir un autómata desde una expresión regular
 * @author Pablo
 */
public class AutomataMain {
    //se define una variable global para la transicion epsilon
    public static String EPSILON = "ε";
    public static char EPSILON_CHAR = EPSILON.charAt(0);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
      

        String regex = JOptionPane.showInputDialog(
           null,
           "ingrese la expresión regular, el string epsilon es: " + EPSILON,
           "(a|b)*abb");  // el icono sera un iterrogante

          

        String cadena = JOptionPane.showInputDialog(
            null,
            "Ingrese la cadena w a simular",
            "abbc");  // el icono sera un iterrogante
            
               
        
       
        Scanner teclado = new Scanner(System.in);
        RegexConverter convert = new RegexConverter();
        try{
            
           regex = convert.infixToPostfix(regex);
            
        }catch(Exception e){
            System.out.println("Expresión mal ingresada");
        }
       
        AFNConstruct ThomsonAlgorithim = new AFNConstruct(regex);
        //aplicar el algoritmo de thomson para crear el automata
        double afnCreateStart = System.currentTimeMillis();
        ThomsonAlgorithim.construct();
        double afnCreateStop = System.currentTimeMillis();
        
        
       //obtener el AFN resultante
        Automata afn_result = ThomsonAlgorithim.getAfn();
        System.out.println(afn_result);
        System.out.println("");
        System.out.println("Construcción AFN: " + (afnCreateStop-afnCreateStart)+" ms");
        
         Simulacion simulador = new Simulacion();
        //Simular el AFN
         
        double afnSimulateStart = System.currentTimeMillis();
        simulador.simular(cadena,afn_result);
        double afnSimulateStop = System.currentTimeMillis();
        System.out.println("Simulación AFN: " + (afnSimulateStop-afnSimulateStart) + " ms");
        
        //CREAr TXT y DOT
        crearArchivos(afn_result, (afnCreateStop-afnCreateStart), (afnSimulateStop-afnSimulateStart), "AFN");
        
       
        AFDConstructor AFD = new AFDConstructor();
        
        //convertir el AFN a AFD
        double afdConvertStart = System.currentTimeMillis();
        AFD.conversionAFN(afn_result);
        double afdConvertStop = System.currentTimeMillis();
        System.out.println("");
        System.out.println("Conversión a AFD: " + (afdConvertStop-afdConvertStart)+" ms");
        //obtener el AFD resultante
        Automata afd_result = AFD.getAfd();
        //afn_result.generarDOT("Test");
        System.out.println("");
       
     
        //Simular el AFD
        double afdSimulateStart = System.currentTimeMillis();
        simulador.simular(cadena,afd_result);
        double afdSimulateStop = System.currentTimeMillis();
        System.out.println("Simulación AFD: " + (afdSimulateStop-afdSimulateStart)+ " ms");
        System.out.println("");
        
        crearArchivos(afd_result, (afdConvertStop-afdConvertStart), (afdSimulateStop-afdSimulateStart), "AFD Subconjuntos");
     
        //versión extendida para generar el árbol sintáctico
        String regexExtended = regex+"#.";
       
        
        SyntaxTree syntaxTree = new SyntaxTree();
        syntaxTree.buildTree(regexExtended);
      
        System.out.println(syntaxTree.getRoot().postOrder());
      
        //creación directa del AFD
        double afdDirectStart = System.currentTimeMillis();
        AFD.creacionDirecta(syntaxTree);
        double afdDirectStop = System.currentTimeMillis();
        
        Automata afd_directo = AFD.getAfdDirecto();
        //simulación de la creación Directa AFD
        double afdDirectStartSim = System.currentTimeMillis();
        simulador.simular(cadena,afd_directo);
        double afdDirectStopSim = System.currentTimeMillis();
        
       crearArchivos(afd_directo, (afdDirectStop-afdDirectStart),(afdDirectStopSim-afdDirectStartSim),"AFD Directo");
       
       
        
        
        //minimizar el AFD Directo 
        double minTimeStart = System.currentTimeMillis();
        Automata afd_min = AFD.minimizar(afd_directo);
        double minTimeStop = System.currentTimeMillis();
        
        //simular minimización AFD Directo
        double minSimStart = System.currentTimeMillis();
        simulador.simular(regex, afd_min);
        double minSimStop = System.currentTimeMillis();
        
        crearArchivos(afd_min,(minTimeStop-minTimeStart),(minSimStop-minSimStart),"AFD Min Directo");
        
        
        //Minimizar el AFD Subconjuntos
        minTimeStart = System.currentTimeMillis();
        Automata afd_min_sub = AFD.minimizar(afd_result);
        minTimeStop = System.currentTimeMillis();
        
         //simular minimización AFD Directo
        minSimStart = System.currentTimeMillis();
        simulador.simular(regex, afd_min_sub);
        minSimStop = System.currentTimeMillis();
        
         crearArchivos(afd_min_sub,(minTimeStop-minTimeStart),(minSimStop-minSimStart),"AFD Min Subconjuntos");
        
        
      
    }
    /*
    * Método para crear los archivos TXT y DOT
    */
    public static void crearArchivos(Automata tipoAutomata, double tiempoCreacion, double tiempoSimulacion, String tipo){
        
        FileCreator creadorArchivo = new FileCreator();
        Simulacion generadorGrafico = new Simulacion();
        
        creadorArchivo.crearArchivo(tipoAutomata.toString(), tiempoCreacion, tiempoSimulacion, tipo);
        
        generadorGrafico.generarDOT(tipo, tipoAutomata);
        
    }

}
