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
        RegexConverter convert = null ;
        try{
            System.out.println("Ingrese la expresión regular para construir el autómata");
            regex = "(a|b)*abb";
            System.out.println("Ingrese la expresión para simuarlo");
            regexSimulacion = "";
            convert = new RegexConverter();


            System.out.println(convert.formatRegEx(regex));
            System.out.println(convert.infixToPostfix(regex));
        }catch(Exception e){
            System.out.println("Expresión mal ingresada");
        }
        regex = convert.infixToPostfix(regex);
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
        simulador.simular(afn_result.getEstadoInicial(),regexSimulacion,afn_result.getEstadosAceptacion());
        double afnSimulateStop = System.currentTimeMillis();
        System.out.println("Simulación AFN: " + (afnSimulateStop-afnSimulateStart) + " ms");
        
        //Simular el AFD
        double afdSimulateStart = System.currentTimeMillis();
        simulador.simular(afd_result.getEstadoInicial(), regexSimulacion, afd_result.getEstadosAceptacion());
        double afdSimulateStop = System.currentTimeMillis();
        System.out.println("Simulación AFD: " + (afdSimulateStop-afdSimulateStart)+ " ms");
        System.out.println("");
        
        //Creamos el archivo de AFN(true) y AFD(false)
        FileCreator creadorArchivo = new FileCreator();
        creadorArchivo.crearArchivo(afn_result.toString(), afnCreateStop-afnCreateStart, afnSimulateStop-afnSimulateStart, true);
        creadorArchivo.crearArchivo(afd_result.toString(), afdConvertStop-afdConvertStart, afdSimulateStop-afdSimulateStart, false);
        
        
       
        
        String regexExtended = regex+"#.";
       
        
        SyntaxTree syntaxTree = new SyntaxTree();
        syntaxTree.buildTree(regexExtended);
      
        System.out.println(syntaxTree.getRoot());
       
       
    }

}
