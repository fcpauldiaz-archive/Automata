package thomson;
/**
 * Clase que construye el árbol sintáctico
 * @author Pablo
 * @param <T> Generic
 */
public class SyntaxTree<T> {

    private Nodo<String> root;
    
    
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
        buildBranch((Nodo<T>) this.root);

        
    }

    /**
     * Método que construye las ramas de árbol 
     * @param nodo 
     */
    private void buildBranch(Nodo<T> nodo) {
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
                nodo.setIzquierda(new Nodo(this.obtener_operando(sub_cadena)));
                //para generar recursivamente el nodo
                buildBranch(nodo.getIzquierda());

                //el hijo izquierdo dejarlo vacío...
                nodo.setDerecha(new Nodo(""));
              
                nodo.getDerecha().setId("");
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

    public Nodo<String> getRoot() {
        return root;
    }

    public void setRoot(Nodo<String> root) {
        this.root = root;
    }
    
    
  
   
    
}