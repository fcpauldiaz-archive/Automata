/**
* Universidad del Valle de Guatemala
* Pablo Diaz 13203
* 2/07/2015
* a través del algoritmo de Thomson
*/

package thomson;

import javax.swing.JOptionPane;

/**
 * Proyecto para construir un autómata desde una expresión regular
 * @author Pablo
 */
public class AFNThomsonMain {
    //se define una variable global para la transicion epsilon
    public static String EPSILON = "ε";
    public static char EPSILON_CHAR = EPSILON.charAt(0);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        String regex = "(a|b)*abb";
        RegexConverter convert = new RegexConverter();
        
        //System.out.println(convert.abreviaturaInterrogacion(regex));
        //System.out.println(convert.abreviaturaCerraduraPositiva(regex));
        //System.out.println(convert.abreviaturaCerraduraPositiva(convert.abreviaturaInterrogacion(regex)));
        System.out.println(convert.formatRegEx(regex));
        System.out.println(convert.infixToPostfix(regex));
        AFNConstruct ThomsonAlgorithim = new AFNConstruct(regex);
        ThomsonAlgorithim.construct();
        AFN afn_result = ThomsonAlgorithim.getAfn();
        ThomsonAlgorithim.simular("abb");
        FileCreator crearArchivo = new FileCreator(afn_result.toString());
        AFDConstructor AFD = new AFDConstructor();
        AFD.convertAFN(afn_result);
    }

}
