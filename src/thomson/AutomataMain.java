/**
* Universidad del Valle de Guatemala
* Pablo Diaz 13203
* 2/07/2015
* a través del algoritmo de Thomson
*/

package thomson;

import java.util.Scanner;

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
        
        String regex = "";
        String regexSimulacion = "";
        Scanner teclado = new Scanner(System.in);
        try{
            System.out.println("Ingrese la expresión regular para construir el autómata");
            regex = teclado.next();
            System.out.println("Ingrese la expresión para simuarlo");
            regexSimulacion = teclado.next();
            RegexConverter convert = new RegexConverter();


            System.out.println(convert.formatRegEx(regex));
            System.out.println(convert.infixToPostfix(regex));
        }catch(Exception e){
            System.out.println("Expresión mal ingresada");
        }
        
        AFNConstruct ThomsonAlgorithim = new AFNConstruct(regex);
        //aplicar el algoritmo de thomson para crear el automata
        long afnCreateStart = System.currentTimeMillis();
        ThomsonAlgorithim.construct();
        long afnCreateStop = System.currentTimeMillis();
       //obtener el AFN resultante
        Automata afn_result = ThomsonAlgorithim.getAfn();
        System.out.println(afn_result);
        
       
        AFDConstructor AFD = new AFDConstructor();
        //convertir el AFN a AFD
        long afdConvertStart = System.currentTimeMillis();
        AFD.conversionAFN(afn_result);
        long afdConvertStop = System.currentTimeMillis();
        //obtener el AFD resultante
        Automata afd_result = AFD.getAfd();
        
        Simulacion simulador = new Simulacion();
        //Simular el AFN
        long afnSimulateStart = System.currentTimeMillis();
        simulador.simular(afn_result.getEstadoInicial(),regexSimulacion,afn_result.getEstadosAceptacion());
        long afnSimulateStop = System.currentTimeMillis();
        
        //Simular el AFD
        long afdSimulateStart = System.currentTimeMillis();
        simulador.simular(afd_result.getEstadoInicial(), regexSimulacion, afd_result.getEstadosAceptacion());
        long afdSimulateStop = System.currentTimeMillis();
        
        //Creamos el archivo de AFN(true) y AFD(false)
        FileCreator creadorArchivo = new FileCreator();
        creadorArchivo.crearArchivo(afn_result.toString(), afnCreateStop-afnCreateStart, afnSimulateStop-afnSimulateStart, true);
        creadorArchivo.crearArchivo(afd_result.toString(), afdConvertStop-afdConvertStart, afdSimulateStop-afdSimulateStart, false);
    }

}
