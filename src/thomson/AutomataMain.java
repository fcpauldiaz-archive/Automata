/**
* Universidad del Valle de Guatemala
* Pablo Diaz 13203
* 2/07/2015
* a través del algoritmo de Thomson
*/

package thomson;

import java.util.ArrayList;
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
        
        
        String regex = "(a|b)*abb";
        String regexSimulacion = "abb";
        RegexConverter convert = new RegexConverter();
        
        
        System.out.println(convert.formatRegEx(regex));
        System.out.println(convert.infixToPostfix(regex));
        
        
        AFNConstruct ThomsonAlgorithim = new AFNConstruct(regex);
        long afnCreateStart = System.currentTimeMillis();
        ThomsonAlgorithim.construct();
        long afnCreateStop = System.currentTimeMillis();
       
        AFN afn_result = ThomsonAlgorithim.getAfn();
        System.out.println(afn_result);
        Simulacion simulador = new Simulacion();
        
       
        AFDConstructor AFD = new AFDConstructor();
        
        long afdConvertStart = System.currentTimeMillis();
        AFD.conversionAFN(afn_result);
        long afdConvertStop = System.currentTimeMillis();
        AFD afd_result = AFD.getAfd();
        
        long afnSimulateStart = System.currentTimeMillis();
        simulador.simular(afn_result.getEstadoInicial(),regexSimulacion,afn_result.getEstadoFinal());
        long afnSimulateStop = System.currentTimeMillis();
        
        
        long afdSimulateStart = System.currentTimeMillis();
        simulador.simular(afd_result.getEstadoInicial(), regexSimulacion, afd_result.getEstadosAceptacion());
        long afdSimulateStop = System.currentTimeMillis();
        
        
        FileCreator creadorArchivo = new FileCreator();
        creadorArchivo.crearArchivo(afn_result.toString(), afnCreateStop-afnCreateStart, afnSimulateStop-afnSimulateStart, true);
        creadorArchivo.crearArchivo(afd_result.toString(), afdConvertStop-afdConvertStart, afdSimulateStop-afdSimulateStart, false);
    }

}
