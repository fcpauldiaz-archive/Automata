/**
* Universidad del Valle de Guatemala
* Pablo Diaz 13203
*/

package thomson;

/**
 *
 * @author Pablo
 */
public class AFNThomson {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        RegexConverter convert = new RegexConverter();
        String cadena = "ac|b";
        //"bb*.a.b.c.c*.";
//        System.out.println(convert.abreviaturaInterrogacion(cadena));
          //System.out.println(convert.abreviaturaCerraduraPositiva(cadena));
          //System.out.println(convert.abreviaturaCerraduraPositiva(convert.abreviaturaInterrogacion(cadena)));
          //System.out.println(convert.formatRegEx(cadena));
          //System.out.println(convert.infixToPostfix(cadena));
          TConstruct construct = new TConstruct(cadena);
          construct.construct();
    }

}
