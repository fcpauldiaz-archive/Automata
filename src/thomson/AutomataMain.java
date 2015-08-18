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
        Simulacion simulador = new Simulacion();
        
        //Simular el AFN
        double afnSimulateStart = System.currentTimeMillis();
        simulador.simular(cadena,afn_result);
        double afnSimulateStop = System.currentTimeMillis();
        System.out.println("Simulación AFN: " + (afnSimulateStop-afnSimulateStart) + " ms");
        
        //Simular el AFD
        double afdSimulateStart = System.currentTimeMillis();
        simulador.simular(cadena,afd_result);
        double afdSimulateStop = System.currentTimeMillis();
        System.out.println("Simulación AFD: " + (afdSimulateStop-afdSimulateStart)+ " ms");
        System.out.println("");
        
        //Creamos el archivo de AFN(true) y AFD(false)
        FileCreator creadorArchivo = new FileCreator();
        creadorArchivo.crearArchivo(afn_result.toString(), afnCreateStop-afnCreateStart, afnSimulateStop-afnSimulateStart, "AFN");
        creadorArchivo.crearArchivo(afd_result.toString(), afdConvertStop-afdConvertStart, afdSimulateStop-afdSimulateStart, "AFD");
        
        
        String regexExtended = regex+"#.";
       
        
        SyntaxTree syntaxTree = new SyntaxTree();
        syntaxTree.buildTree(regexExtended);
      
        System.out.println(syntaxTree.getRoot().postOrder());
      
        
        double afdDirectStart = System.currentTimeMillis();
        AFD.creacionDirecta(syntaxTree);
        double afdDirectStop = System.currentTimeMillis();
        
        Automata afd_directo = AFD.getAfdDirecto();
        
        double afdDirectStartSim = System.currentTimeMillis();
        simulador.simular(cadena,afd_directo);
        double afdDirectStopSim = System.currentTimeMillis();
        
        creadorArchivo.crearArchivo(AFD.getAfdDirecto().toString(), afdDirectStop-afdDirectStart, afdDirectStopSim-afdDirectStartSim, "");
        
        simulador.generarDOT("AFN", afn_result);
        simulador.generarDOT("AFD_Subconjuntos", afd_result);
        simulador.generarDOT("AFD_Directo", afd_directo);
       
        //AFD.minimizacionAFD();
        
        simulador.generarDOT("afn", afn_result);
        Automata afd_min = AFD.getAfdMinimo();
        
        simulador.generarDOT("afd minimio", afd_min);
    }

}
