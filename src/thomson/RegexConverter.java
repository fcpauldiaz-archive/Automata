/**
* Universidad Del Valle de Guatemala
* Pablo Díaz 13203
* 29/07/2015
*/

package thomson;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Conversión de una expresión regular infix a postfix
 * @author GMenard
 * Reference: https://gist.github.com/gmenard/6161825
 * Modified by
 * @author fcpauldiaz
 * @since 29/07/2015
 * Includes abreviatures to reduce operators
 */
public class RegexConverter {
    
    /** Operators precedence map. */
	private final Map<Character, Integer> procedenciaOperadores;
        
        //constructor
	public RegexConverter()
        {
		Map<Character, Integer> map = new HashMap<>();
		map.put('(', 1); // parentesis
		map.put('|', 2); // Union o or
		map.put('.', 3); // explicit concatenation operator
		map.put('?', 4); // | €
		map.put('*', 4); // kleene
		map.put('+', 4); // positivo
		procedenciaOperadores = Collections.unmodifiableMap(map);
                
              
	};
        
        
	/**
	 * Obtener la precedencia del caracter
	 * 
	 * @param c character
	 * @return corresponding precedence
	 */
	private Integer getPrecedencia(Character c) {
		Integer precedencia = procedenciaOperadores.get(c);
                //si obtiene un valor nulo retrona 6 por default
		return precedencia == null ? 6 : precedencia;
	}

        /**
         * Remover caracter en la posicion deseada
         * @param s string deseado
         * @param pos indice eel caracter
         * @return nuevo string sin un caracter
         */
        private String removeCharAt(String s, int pos) {
            return s.substring(0, pos) + s.substring(pos + 1);
        }
        /**
         * Insertar caracter en una posicion deseada
         * @param s string deseado
         * @param pos indice del caracter
         * @param ch caracter deseado
         * @return nuevo string con el caracter deseado
         */
        private String insertCharAt(String s, int pos, Object ch){
            return s.substring(0,pos)+ch+s.substring(pos+1);
        }
        /**
         * Agregar caracter en la posicion deseada (no elimina el caracter anterior)
         * @param s string deseado
         * @param pos posicion del caracter
         * @param ch caracter deseado
         * @return nuevo string con el caracter agregado
         */
        private String appendCharAt(String s, int pos, Object ch){
            String val = s.substring(pos,pos+1);
            return s.substring(0,pos)+val+ch+s.substring(pos+1);
            
        }
        
        /**
         * Metodo para abreviar el operador ? 
         * equivalente a |€
         * @param regex expresion regular
         * @return expresion regular modificada sin el operador ?
         */
        public String abreviaturaInterrogacion(String regex)
        {   
            for (int i = 0; i<regex.length();i++){
                 Character ch = regex.charAt(i);
                 
                  if (ch.equals('?'))
                {
                    if (regex.charAt(i-1) == ')'){
                        regex = insertCharAt(regex,i,"|ε)");
                        
                        int j =i;
                        while (j!=0){
                            if (regex.charAt(j)=='(')
                            {
                                break;
                            }
                            
                        j--;
                        
                        }
                        
                        regex=appendCharAt(regex,j,"(");
                         
                    }
                    else
                    {
                        regex = insertCharAt(regex,i,"|ε)");
                        regex = insertCharAt(regex,i-1,"("+regex.charAt(i-1));
                    }
                }
            }
            
            return regex;
        }
        
        /**
         * Método para abreviar el operador de cerradura positiva
         * @param regex
         * @return expresion regular modificada sin el operador +
         */
        public String abreviaturaCerraduraPositiva(String regex){
            String regexAbreviated = new String();
            
            for (int i = 0; i<regex.length();i++){
                 Character ch = regex.charAt(i);
                 
                if (ch.equals('+'))
                {
                    regexAbreviated += regex.charAt(i-1);
                    regex = insertCharAt(regex,i,'*');
                   
                }
                regexAbreviated += regex.charAt(i);
            }
            
            return regexAbreviated;
        }
	/**
	 * 
         * Transformar una expresión regular insertando un punto '.' explicitamente
         * como operador de concatenación.
         * @param regex String
         * @return regexExplicit String 
	 */
	public  String formatRegEx(String regex) {
                regex = abreviaturaInterrogacion(regex);
                regex = abreviaturaCerraduraPositiva(regex);
		String  regexExplicit = new String();
		List<Character> operadores = Arrays.asList('|', '?', '+', '*');
		List<Character> operadoresBinarios = Arrays.asList('|');
                
                
                //recorrer la cadena
		for (int i = 0; i < regex.length(); i++)
                {
                    Character c1 = regex.charAt(i);
                   
                    if (i + 1 < regex.length()) 
                    {
                        
                        Character c2 = regex.charAt(i + 1);
                        
                        regexExplicit += c1;
                        
                        //mientras la cadena no incluya operadores definidos, será una concatenación implicita
                        if (!c1.equals('(') && !c2.equals(')') && !operadores.contains(c2) && !operadoresBinarios.contains(c1))
                        {
                            regexExplicit += '.';
                           
                        }
                        
                    }
		}
		regexExplicit += regex.charAt(regex.length() - 1);
                

		return regexExplicit;
	}
        
        /**
	 * Convertir una expresión regular de notación infix a postfix 
	 * con el algoritmo de Shunting-yard. 
	 * 
	 * @param regex notacion infix 
	 * @return notacion postfix 
	 */
	public  String infixToPostfix(String regex) {
		String postfix = new String();

		Stack<Character> stack = new Stack<>();

		String formattedRegEx = formatRegEx(regex);

		for (Character c : formattedRegEx.toCharArray()) {
			switch (c) {
				case '(':
					stack.push(c);
					break;

				case ')':
					while (!stack.peek().equals('(')) {
						postfix += stack.pop();
					}
					stack.pop();
					break;

				default:
					while (stack.size() > 0) 
                                        {
						Character peekedChar = stack.peek();

						Integer peekedCharPrecedence = getPrecedencia(peekedChar);
						Integer currentCharPrecedence = getPrecedencia(c);

						if (peekedCharPrecedence >= currentCharPrecedence) 
                                                {
							postfix += stack.pop();
                                                       
						} 
                                                else 
                                                {
							break;
						}
					}
					stack.push(c);
					break;
			}

		}

		while (stack.size() > 0)
			postfix += stack.pop();

		return postfix;
	}

}
