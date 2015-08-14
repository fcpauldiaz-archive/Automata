package thomson;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Clase que construye el árbol sintáctico
 * @author Pablo
 * @param <T> Generic
 */
public class SyntaxTree<T> {

    private Nodo<T> root;
    private Nodo<T> actual;
    Queue pila = new LinkedList();
    
    
   /**
    * Constructor 
    * inicializa la raiz del arbol
    */
    public SyntaxTree(){
        this.root= new Nodo("");
    }

   /**
    * Método que construye el árbol a partir de una cadena 
    * @param cadenaEnPrefix
    */
    public void buildTree(String cadenaEnPrefix){
        
        this.root = new Nodo(cadenaEnPrefix);
        buildPostFix((Nodo<T>) this.root);
        this.root=this.actual;
        

        
    }

    /**
     * Método que construye las ramas de árbol 
     * @param nodo 
     */
    private void buildBranch(Nodo<T> nodo) {
        System.out.println(this.root);
        String texto_prefix = (String) nodo.getRegex();
        char letra_inicial = texto_prefix.charAt(0);
        System.out.println("letra inicial: " + letra_inicial);
        //verificar si es un símbolo. Si lo es poner de una vez en la rama
        if(letra_inicial!='*'&&letra_inicial!='|'&&letra_inicial!='.'){
            nodo.setId((T) (""+letra_inicial));
            nodo.setIsLeaf(true);
           
        }
        else if (!nodo.isIsLeaf())//verificar que no sea terminal
            
        {
            //es un operador

            //si es un operador unario (como *)
            if(letra_inicial == '*'){
                //obtener un operador
                //se le asigna el nombre al nodo principal
                nodo.setId((T) (""+letra_inicial));
                

                String sub_cadena = texto_prefix.substring(1);//falta validar...
                //print("subcadena: "+sub_cadena);
                nodo.setIzquierda(new Nodo(obtener_operando(sub_cadena)));
                //para generar recursivamente el nodo
                buildBranch(nodo.getIzquierda());

           
            }

            //si es un operador unario (como |, concat)
            else if(letra_inicial=='|'||letra_inicial=='.'){
                //obtener dos operadores

                //se le asigna el nombre al nodo principal
                nodo.setId((T) (""+letra_inicial));
               
                String sub_cadena = texto_prefix.substring(1);
                System.out.println("subcadena " + sub_cadena);
                String primer_operando = this.obtener_operando(sub_cadena);
                String segundo_operando = this.obtener_operando(sub_cadena.substring(primer_operando.length()));

              

                nodo.setIzquierda(new Nodo(primer_operando));
                //para generar recursivamente el nodo hijo izquierdo
                buildBranch(nodo.getIzquierda());

                //el hijo izquierdo dejarlo vacío...
                nodo.setDerecha(new Nodo(segundo_operando));
                //para generar recursivamente el nodo hijo izquierdo
                buildBranch(nodo.getDerecha());
            }
        }//cierra else if leaf
    }

    /**
     * obtiene el primer operando que esté en la cadena
     * @param texto
     * @return 
     */
    public String obtener_operando(String texto) {

        char primero = texto.charAt(0);

        if(primero!='*'&&primero!='|'&&primero!='.'){
            return ""+primero;
        }

        int contador=0;
        if(primero=='|'||primero=='.'){
            contador = 2;
        }
        else if(primero=='*'){
            contador = 1;
        }
        //System.out.println("contadodr:"+contador);
        String cadena = ""+primero;

        //restar 1 si encuentra simbolo, sumar 2 si encuentra op binario, sumar 1 si encuentra op unario
        for(int i=1; contador>0 && i<texto.length() ;i++){
            //print("contador"+contador);
            char letra = texto.charAt(i);
            //print(""+letra);
            if(letra!='*'&&letra!='|'&&letra!='.'){
                contador--;
                cadena+=""+letra;
            }
            else if(letra=='*'){
                contador+=0;//todavía le falta 1 operando..
                cadena+=""+letra;
            }
            else if(letra=='.'||letra=='|'){
                contador+=1;//agrega un operando más...
                cadena+=""+letra;
            }
        }

        return cadena;
    }

    public Nodo<T> getRoot() {
        return root;
    }

    public void setRoot(Nodo<T> root) {
        this.root = root;
    }
    
    
    private void buildPostFix(Nodo<T> nodo){
        System.out.println(this.root);
        String texto_prefix = (String) nodo.getRegex();
        char letra_inicial = texto_prefix.charAt(0);
        System.out.println("letra inicial: " + letra_inicial);
        //verificar si es un símbolo. Si lo es poner de una vez en la rama
        if(letra_inicial!='*'&&letra_inicial!='|'&&letra_inicial!='.'){
            
            String sub_cadena = texto_prefix.substring(1);
            System.out.println(sub_cadena);
            Nodo nuevo = new Nodo((sub_cadena));
            nuevo.setId(""+letra_inicial);
            //nuevo.setIsLeaf(true);
            if (pila.isEmpty())
                pila.add(nodo);
            pila.remove(this.root);
            pila.add(nuevo);
            buildPostFix(nuevo);
           
           
        }
         else//verificar que no sea terminal
            
        {
            //es un operador

            //si es un operador unario (como *)
            if(letra_inicial == '*'){
                //obtener un operador
                //se le asigna el nombre al nodo principal
                String sub_cadena = texto_prefix.substring(1);
                Nodo nuevo = new Nodo(sub_cadena);
                nuevo.setId((T) (""+letra_inicial));
                
                Nodo nodoPila = nodo;
                nuevo.setIzquierda(nodoPila);
                
                /*String sub_cadena = texto_prefix.substring(1);//falta validar...
                //print("subcadena: "+sub_cadena);
                nodo.setIzquierda(new Nodo(obtener_operando(sub_cadena)));
                //para generar recursivamente el nodo*/
                pila.add(nuevo);
               
               

           
            }

            //si es un operador unario (como |, concat)
            else if(letra_inicial=='|'||letra_inicial=='.'){
                //obtener dos operadores

                //se le asigna el nombre al nodo principal
               // nodo.setId((T) (""+letra_inicial));
               
               
                String sub_cadena = texto_prefix.substring(1);
                System.out.println("subcadena " + sub_cadena);
               /* String primer_operando = this.obtener_operando(sub_cadena);
                String segundo_operando = this.obtener_operando(sub_cadena.substring(primer_operando.length()));*/
                Nodo nuevo = new Nodo(sub_cadena);
                nuevo.setId(""+letra_inicial);
              
                nuevo.setIzquierda((Nodo) pila.poll());
               
                //nodo.setIzquierda(new Nodo(primer_operando));
                //para generar recursivamente el nodo hijo izquierdo
                System.out.println(nuevo.getIzquierda());
                //buildPostFix(nuevo.getIzquierda());
                if (!pila.isEmpty())
                    nuevo.setDerecha((Nodo)pila.poll());
                else
                    nuevo.setDerecha(nodo);
                //el hijo izquierdo dejarlo vacío...
                //odo.setDerecha(new Nodo(segundo_operando));
                //para generar recursivamente el nodo hijo izquierdo
               // buildPostFix(nuevo.getDerecha());
                 pila.add(nuevo);
                 this.actual = nuevo;
                
            }
        }//cierra else if leaf
        if (!pila.isEmpty()){
            Nodo siguiente = (Nodo) pila.poll();
            if (!siguiente.getRegex().equals("")){
                buildPostFix(siguiente);
                
            }
            
        }
        
        
        
        
    }

    public Nodo<T> getResultado() {
        return actual;
    }

    public void setResultado(Nodo<T> resultado) {
        this.actual = resultado;
    }
   
    
}