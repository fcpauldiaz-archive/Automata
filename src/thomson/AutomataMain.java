/**
* Universidad del Valle de Guatemala
* Pablo Diaz 13203
* 2/07/2015
* a través del algoritmo de Thomson
*/

package thomson;

import java.util.HashMap;
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
      
        ReadFile read = new ReadFile();
        HashMap input = read.leerArchivo();
        LexerAnalyzer lexer = new LexerAnalyzer(input);
        lexer.vocabulario();
        lexer.check(input);  
       
        /*  
        String regex = JOptionPane.showInputDialog(
           null,
           "ingrese la expresión regular, el string epsilon es: " + EPSILON,
           "(a|b)*abb");  // el icono sera un iterrogante

          

        String cadena = JOptionPane.showInputDialog(
            null,
            "Ingrese la cadena w a simular",
            "abbc");  // el icono sera un iterrogante
            
               
      
       String lenguaje = regex;
        Scanner teclado = new Scanner(System.in);
        RegexConverter convert = new RegexConverter();
        try{
            
           regex = convert.infixToPostfix(regex);
            
        }catch(Exception e){
            System.out.println("Expresión mal ingresada");
        }
       
        AFNConstruct ThomsonAlgorithim = new AFNConstruct(regex);
        //aplicar el algoritmo de thomson para crear el automata
        double afnCreateStart = System.nanoTime();
        ThomsonAlgorithim.construct();
        double afnCreateStop = System.nanoTime();
        
        System.out.println(regex);
       //obtener el AFN resultante
        Automata afn_result = ThomsonAlgorithim.getAfn();
        System.out.println(afn_result);
        System.out.println("");
        System.out.println("Construcción AFN: " + (afnCreateStop-afnCreateStart)+" ms");
        
         Simulacion simulador = new Simulacion();
        //Simular el AFN
         
        double afnSimulateStart = System.nanoTime();
        simulador.simular(cadena,afn_result);
        double afnSimulateStop = System.nanoTime();
        System.out.println("Simulación AFN: " + (afnSimulateStop-afnSimulateStart) + " ms");
        
        afn_result.addResultadoRegex(0, lenguaje);
        afn_result.addResultadoRegex(1, cadena);
        afn_result.addResultadoRegex(2, simulador.getResultado());
        
        
        //CREAr TXT y DOT
        crearArchivos(afn_result, (afnCreateStop-afnCreateStart), (afnSimulateStop-afnSimulateStart), "AFN");
        
       
        AFDConstructor AFD = new AFDConstructor();
        
        //convertir el AFN a AFD
        double afdConvertStart = System.nanoTime();
        AFD.conversionAFN(afn_result);
        double afdConvertStop = System.nanoTime();
        System.out.println("");
        System.out.println("Conversión a AFD: " + (afdConvertStop-afdConvertStart)+" ms");
        //obtener el AFD resultante
        Automata afd_result = AFD.getAfd();
        //afn_result.generarDOT("Test");
      
     
        //Simular el AFD
        double afdSimulateStart = System.nanoTime();
        simulador.simular(cadena,afd_result);
        double afdSimulateStop = System.nanoTime();
        System.out.println("Simulación AFD: " + (afdSimulateStop-afdSimulateStart)+ " ms");
        System.out.println("");
        
        afd_result.addResultadoRegex(0, lenguaje);
        afd_result.addResultadoRegex(1, cadena);
        afd_result.addResultadoRegex(2, simulador.getResultado());
        
        crearArchivos(afd_result, (afdConvertStop-afdConvertStart), (afdSimulateStop-afdSimulateStart), "AFD Subconjuntos");
     
        //versión extendida para generar el árbol sintáctico
        String regexExtended = regex+"#.";
       
        
        SyntaxTree syntaxTree = new SyntaxTree();
        syntaxTree.buildTree(regexExtended);
      
        System.out.println(syntaxTree.getRoot().postOrder());
      
        //creación directa del AFD
        double afdDirectStart = System.nanoTime();
        AFD.creacionDirecta(syntaxTree);
        double afdDirectStop = System.nanoTime();
        
        Automata afd_directo = AFD.getAfdDirecto();
        //simulación de la creación Directa AFD
        double afdDirectStartSim = System.nanoTime();
        simulador.simular(cadena,afd_directo);
        double afdDirectStopSim = System.nanoTime();
        
        afd_directo.addResultadoRegex(0, lenguaje);
        afd_directo.addResultadoRegex(1, cadena);
        afd_directo.addResultadoRegex(2, simulador.getResultado());
        
       crearArchivos(afd_directo, (afdDirectStop-afdDirectStart),(afdDirectStopSim-afdDirectStartSim),"AFD Directo");
       
       
        
        
        //minimizar el AFD Directo 
        double minTimeStart = System.nanoTime();
        Automata afd_min = AFD.minimizar(afd_directo);
        double minTimeStop = System.nanoTime();
        
        //simular minimización AFD Directo
        double minSimStart = System.nanoTime();
        simulador.simular(regex, afd_min);
        double minSimStop = System.nanoTime();
        
        afd_min.addResultadoRegex(0, lenguaje);
        afd_min.addResultadoRegex(1, cadena);
        afd_min.addResultadoRegex(2, simulador.getResultado());
        
        crearArchivos(afd_min,(minTimeStop-minTimeStart),(minSimStop-minSimStart),"AFD Min Directo");
        
        
        //Minimizar el AFD Subconjuntos
        minTimeStart = System.nanoTime();
        Automata afd_min_sub = AFD.minimizar(afd_result);
        minTimeStop = System.nanoTime();
        
         //simular minimización AFD Directo
        minSimStart = System.nanoTime();
        simulador.simular(regex, afd_min_sub);
        minSimStop = System.nanoTime();
        
        afd_min_sub.addResultadoRegex(0, lenguaje);
        afd_min_sub.addResultadoRegex(1, cadena);
        afd_min_sub.addResultadoRegex(2, simulador.getResultado());
        
         crearArchivos(afd_min_sub,(minTimeStop-minTimeStart),(minSimStop-minSimStart),"AFD Min Subconjuntos");
        
        System.out.println("");
        Automata afd_trampa = AFD.quitarEstadosTrampa(afd_result);
        
           //simular minimización AFD Directo
        minSimStart = System.nanoTime();
        simulador.simular(regex, afd_trampa);
        minSimStop = System.nanoTime();
        
        crearArchivos(afd_trampa,(minTimeStop-minTimeStart),(minSimStop-minSimStart),"AFD SubConjuntos Sin Estados Trampa");
        
        System.out.println("");
        
        Automata afd_dirtrampa = AFD.quitarEstadosTrampa(afd_directo);
           //simular minimización AFD Directo
        minSimStart = System.nanoTime();
        simulador.simular(regex, afd_dirtrampa);
        minSimStop = System.nanoTime();
        crearArchivos(afd_dirtrampa,(minTimeStop-minTimeStart),(minSimStop-minSimStart),"AFD Directo Sin Estados Trampa");
      */
    }
    /*
    * Método para crear los archivos TXT y DOT
    */
    public static void crearArchivos(Automata tipoAutomata, double tiempoCreacion, double tiempoSimulacion, String tipo){
        
        FileCreator creadorArchivo = new FileCreator();
        Simulacion generadorGrafico = new Simulacion();
        System.out.println("INTENTO");
        creadorArchivo.crearArchivo(tipoAutomata.toString(), tiempoCreacion, tiempoSimulacion, tipo);
        
        generadorGrafico.generarDOT(tipo, tipoAutomata);
        
    }

}
