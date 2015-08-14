/**
* Universidad Del Valle de Guatemala
* 12-ago-2015
* Pablo Díaz 13203
*/

package thomson;

/**
 *
 * @author Pablo
 * @param <T>
 */
public class Nodo<T> {
    
    private Nodo izquierda, derecha;
    private boolean isLeaf;
    private T id;
    private T regex;
   
    public Nodo(T regex) {
        this.regex = regex;
        this.izquierda= new Nodo();
        this.derecha = new Nodo();
        
        
    }

    public Nodo(){
        
        
    }
    public Nodo getIzquierda() {
        return izquierda;
    }

    public void setIzquierda(Nodo izquierda) {
        this.izquierda = izquierda;
    }

    public Nodo getDerecha() {
        return derecha;
    }

    public void setDerecha(Nodo derecha) {
        this.derecha = derecha;
    }

    public boolean isIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public T getRegex() {
        return regex;
    }

    public void setRegex(T regex) {
        this.regex = regex;
    }

    @Override
    public String toString() {
        String regexd = Mostrar_Nodo();
        String regexPreFix = new StringBuilder(regexd).reverse().toString();
       return regexd;
        
    }

    
      public String preOrder()
    {
        String res = "";
         
            if (id!=null)
                res += getId();
            if (izquierda!=null)
                res += getIzquierda();
           
            if (derecha!=null)
                res += getDerecha();
            
            return res;
     //"b.b.a.b|a*.#"  
    }
   
    
    public String Mostrar_Nodo() {

        String temp="";

        if(this.izquierda.getId()!=null){
            temp+=""+this.izquierda.Mostrar_Nodo();
        }
        
        if(this.getId()!=null){
            temp+=this.id+"";
        }

        if(this.derecha.getId()!=null){
            temp+=this.derecha.Mostrar_Nodo()+"";
        }
        return temp;
    }
    
    
    

}
