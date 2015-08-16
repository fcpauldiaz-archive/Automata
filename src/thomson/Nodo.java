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
public class Nodo<T> implements Comparable<Nodo>{
    
    private Nodo izquierda, derecha;
    private boolean isLeaf;
    private T id;
    private T regex;
    private int numeroNodo;
   
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

    public int getNumeroNodo() {
        return numeroNodo;
    }

    public void setNumeroNodo(int numeroNodo) {
        this.numeroNodo = numeroNodo;
    }
    
    

    @Override
    public String toString() {
        String regexd = ""+numeroNodo;
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
     
    }
   
    
    public String postOrder() {

        String res="";

        if(this.izquierda.getId()!=null)
            res+=""+this.izquierda.postOrder();
        
        if(this.id!=null)
            res+=this.id+"";
        
        if(this.derecha.getId()!=null)
            res+=this.derecha.postOrder()+"";
        
        return res;
    }


    @Override
    public int compareTo(Nodo o) {
        return Integer.compare(numeroNodo, o.getNumeroNodo());
    }

   
    
    

}
