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
public class AFNThomson {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        RegexConverter convert = new RegexConverter();
        String regex = "(a|b)*";
        
        
        //System.out.println(convert.abreviaturaInterrogacion(regex));
        //System.out.println(convert.abreviaturaCerraduraPositiva(regex));
        //System.out.println(convert.abreviaturaCerraduraPositiva(convert.abreviaturaInterrogacion(regex)));
        //System.out.println(convert.formatRegEx(regex));
        System.out.println(convert.infixToPostfix(regex));
        TConstruct ThomsonAlgorithim = new TConstruct(regex);
        ThomsonAlgorithim.construct();
    }

}
