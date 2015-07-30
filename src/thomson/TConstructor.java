/**
* Universidad Del Valle Guatemala
* Pablo Díaz 13203
*/

package thomson;

import java.util.Stack;

/**
 *
 * @author Pablo
 */
public class TConstructor {
    
    private final String regex;
    
    
    public TConstructor(String regex)
    {
        RegexConverter convert = new RegexConverter();
        this.regex = convert.infixToPostfix(regex);
       
    }
    
    
    
    

}
