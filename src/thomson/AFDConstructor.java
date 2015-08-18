/**
* Universidad Del Valle de Guatemala
* 07-ago-2015
* Pablo Díaz 13203
*/

package thomson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeSet;

/**
 * Clase que construye un AFD a partir de un AFN
 * @author Pablo
 */
public class AFDConstructor {
    
    private Automata afd;
    private Automata afdDirecto;
    private final Simulacion simulador;
    private HashMap resultadoFollowPos;
  
    
    public AFDConstructor(){
        this.resultadoFollowPos = new HashMap();
        simulador = new Simulacion();
        afd = new Automata();
    }
    
    
    /**
     * Conversion de un automata AFN a uno AFD por el
     * metodo de subconjuntos
     * @param afn AFN
     */
    public void conversionAFN(Automata afn){
        //se crea una estructura vacia
        Automata automata = new Automata();
        //se utiliza una cola como la estructura para guardar los subconjuntos a analizar
        Queue<HashSet<Estado>> cola = new LinkedList();
        //se crea un nuevo estado inicial
        Estado inicial = new Estado(0);
        automata.setEstadoInicial(inicial);
        automata.addEstados(inicial);
       

        //el algoritmo empieza con el e-Closure del estado inicial del AFN
        HashSet<Estado> array_inicial = simulador.eClosure(afn.getEstadoInicial());
        //si el primer e-closure contiene estados de aceptacion hay que agregarlo
        for (Estado aceptacion:afn.getEstadosAceptacion()){
            if (array_inicial.contains(aceptacion))
                automata.addEstadosAceptacion(inicial);
        }
        
        //lo agregamos a la pila
        cola.add(array_inicial);
        //variable temporal para guardar el resultado todos los subconjuntos creados
        ArrayList<HashSet<Estado>> temporal = new ArrayList();
       //se utilizan esetos indices para saber el estado actuales y anterior
       int indexEstadoInicio = 0;
       while (!cola.isEmpty()){
           //actual subconjunto
            HashSet<Estado> actual = cola.poll();
            //se recorre el subconjunto con cada simbolo del alfabeto del AFN
            for (Object simbolo: afn.getAlfabeto())
            {
                //se realiza el move con el subconjunto
                HashSet<Estado> move_result = simulador.move(actual, (String) simbolo);

                HashSet<Estado> resultado = new HashSet();
                //e-Closure con cada estado del resultado del move y 
                //se guarda en un solo array (merge)
                for (Estado e : move_result) 
                {
                    resultado.addAll(simulador.eClosure(e));
                }

                Estado anterior = (Estado) automata.getEstados().get(indexEstadoInicio);
                /*Si el subconjunto ya fue creado una vez, solo se agregan
                transiciones al automata*/
                if (temporal.contains(resultado))
                {
                    ArrayList<Estado> array_viejo = automata.getEstados();
                    Estado estado_viejo = anterior;
                    //se busca el estado correspondiente y se le suma el offset
                    Estado estado_siguiente = array_viejo.get(temporal.indexOf(resultado)+1);
                    estado_viejo.setTransiciones(new Transicion(estado_viejo,estado_siguiente,simbolo));

                }
                //si el subconjunto no existe, se crea un nuevo estado
                else
                {
                    temporal.add(resultado);
                    cola.add(resultado);

                    Estado nuevo = new Estado(temporal.indexOf(resultado)+1);
                    anterior.setTransiciones(new Transicion(anterior,nuevo,simbolo));
                    automata.addEstados(nuevo);
                    //se verifica si el estado tiene que ser de aceptacion
                    for (Estado aceptacion:afn.getEstadosAceptacion()){
                        if (resultado.contains(aceptacion))
                            automata.addEstadosAceptacion(nuevo);
                    }
                }
               

            }
            indexEstadoInicio++;

           }
        
        this.afd = automata;
        //metodo para definir el alfabeto, se copia el del afn
        definirAlfabeto(afn);
        this.afd.setTipo("AFD");
        System.out.println(afd);
        
    }
    
    /**
     * Método general que crea el AFD de forma directa
     * @param arbolSintactico 
     */
    public void creacionDirecta(SyntaxTree arbolSintactico){
        
        //colocar numeracion a los nodos hojas
        generarNumeracionNodos(arbolSintactico);
        
        ArrayList<Nodo> arrayNodos = arbolSintactico.getArrayNodos();      
        
        for (int i = 0;i<arrayNodos.size();i++){
            
            followPos(arrayNodos.get(i));
        }
        toStringFollowPos();
        
        
        crearEstados(arbolSintactico);
        System.out.println("******************************");
        minimizar(automataPrueba());
        
    }
    
    /**
     * Método para verificar si el nodo puede generar epsilon
     * @param expresion
     * @return true si lo puede generar, false si no
     */
    public boolean nullable(Nodo expresion){
        //cerradura de kleene siempre retorna verdadero
        if (expresion.getId().equals(AutomataMain.EPSILON))
            return true;
          //verificar si es una hoja terminal
        else if (expresion.isIsLeaf()==true)
            return false;
        //cuando es or, se verifica cada una las hojas del nodo
        else if (expresion.getId().equals("|"))
            return nullable(expresion.getIzquierda())||nullable(expresion.getDerecha());
        //cuando es concatenacion solo si los dos nodos son true, devuelve true
        else if (expresion.getId().equals("."))
            return nullable(expresion.getDerecha())&&nullable(expresion.getDerecha());
        //si contiene epsilon, es true
        else if (expresion.getId().equals("*"))
            return true;
      
        
        //valor por default a regresar
        return false;
        
    }
    
    /**
     * Devuelve  una lista de elementos que contiene la primera posicion del nodo
     * @param nodoEval
     * @return ArrayList con el resultado
     */
    public TreeSet firstPos(Nodo nodoEval){
        TreeSet resultado = new TreeSet();
        //regresar i en caso de que sea epsilon, regresa vacio
        if (nodoEval.getId().equals(AutomataMain.EPSILON))
            return resultado;
        //en caso de sea una hoja regresa el nodo i en el arreglo
        else if (nodoEval.isIsLeaf()){
            resultado.add(nodoEval);
            return resultado;
        }
        //en caso del OR hace la union de firstPos de los nodos hijos
        else if (nodoEval.getId().equals("|")){
           resultado.addAll(firstPos(nodoEval.getIzquierda()));
           resultado.addAll(firstPos(nodoEval.getDerecha()));
           return resultado;
           
        }
        /*en el caso de la concatenacion primero revisa el nullable y
        despues realiza la union */
        else if (nodoEval.getId().equals(".")){
            if (nullable(nodoEval.getIzquierda())){
                resultado.addAll(firstPos(nodoEval.getIzquierda()));
                resultado.addAll(firstPos(nodoEval.getDerecha()));
            }
            else{
                resultado.addAll(firstPos(nodoEval.getIzquierda()));
            }
        }
        //en el caso de la cerradura de kleene regresa firstPos del nodo hijo izquierdo
        else if (nodoEval.getId().equals("*")){
            resultado.addAll(firstPos(nodoEval.getIzquierda()));
        }
        
        return resultado;
    }
    
    /**
     * Metodo que retorna una lista con los elementos de la operacion
     * last pos del nodo especificado
     * @param nodoEval
     * @return ArrayList con el resultado
     */
    public ArrayList lastPos(Nodo nodoEval){
        ArrayList resultado = new ArrayList();
        
        if (nodoEval.getId().equals(AutomataMain.EPSILON))
            return resultado;
          
        else if (nodoEval.isIsLeaf()){
           resultado.add(nodoEval);
           return resultado;
        }
        else if (nodoEval.getId().equals("*")){
            resultado.addAll(lastPos(nodoEval.getIzquierda()));
        }
        else if (nodoEval.getId().equals("|")){
            resultado.addAll(lastPos(nodoEval.getIzquierda()));
            resultado.addAll(lastPos(nodoEval.getDerecha()));
        }
        else if (nodoEval.getId().equals(".")){
            if (nullable(nodoEval.getDerecha())){
                
                resultado.addAll(lastPos(nodoEval.getIzquierda()));
                resultado.addAll(lastPos(nodoEval.getDerecha()));
            }
            else{
                resultado.addAll(lastPos(nodoEval.getDerecha()));
            }
        }
        
        return resultado;
    }
    
    /**
     * metodo para calcular el follow pos de cada hoja terminal del árbol
     * @param nodoEval
     * 
     */
    public void followPos(Nodo nodoEval){
        //por definicion follow pos aplica para cerradura de kleene y concatenacion
        System.out.println(nodoEval.getId());
        
        //si es cerradura de kleen
        if (nodoEval.getId().equals("*")){
            
            //según el algoritmo primero verificamos el lastPos
            ArrayList<Nodo> lastPosition = lastPos(nodoEval);
            //el follow pos del lastPos incluye todo lo que este en el first pos
            //del kleen
            
            //por lo tanto se necesita el firstPos del kleen
            TreeSet<Nodo> firstPosition = firstPos(nodoEval);
              
            //para agregarlo recorremos los nodos del lastpos  
            for (int i = 0;i<lastPosition.size();i++){
                int numero = (int) lastPosition.get(i).getNumeroNodo();//obtenemos el identificador numerico

                /*si ya se realizo una vez el follow pos de un nodo
                se realiza un merge con el actual y el resultado del follow pos*/
                if (resultadoFollowPos.containsKey(numero)){
                    firstPosition.addAll((Collection) resultadoFollowPos.get(numero));
                    
                
                }
                //y se vuelve a agregar el nuevo resultado
                resultadoFollowPos.put(numero, firstPosition);
                
            }
        }
        //si es concatenación
        else if (nodoEval.getId().equals(".")){
            /*según el algoritmo el follow pos del cada posicion del last pos
            del hijo izquierdo debe incluir el el first pos del hijo derecho*/
            
            //obtener el lastPos del hijo izquierdo
            ArrayList<Nodo> lastPosition = lastPos(nodoEval.getIzquierda());
            //obtener el fistPos del lado derecho
            TreeSet<Nodo> firstPosition = firstPos(nodoEval.getDerecha());
            
            //usamos el last pos del hijo izquierdo 
            for (int i = 0;i<lastPosition.size();i++){
                int numero = (int) lastPosition.get(i).getNumeroNodo();
                //le agregamos el first pos del hijo derecho [merge si ya existe]
                if (resultadoFollowPos.containsKey(numero)){
                    firstPosition.addAll((Collection) resultadoFollowPos.get(numero));//merge
                }
                resultadoFollowPos.put(numero, firstPosition);
            }
            
        }
        
       
    }
    
    /**
     * Método para numerar los nodos hoja del árbol sintáctico
     * @param arbol 
     */
    private void generarNumeracionNodos(SyntaxTree arbol){
         ArrayList<Nodo> arrayNodos = arbol.getArrayNodos();
        int index = 1;
        for (int i = 0 ;i<arrayNodos.size();i++){
            if (arrayNodos.get(i).isIsLeaf()){
                arrayNodos.get(i).setNumeroNodo(index);
                index++;
            }
        }
        for (int i = 0 ;i<arrayNodos.size();i++){
            if (arrayNodos.get(i).isIsLeaf())
                System.out.println( arrayNodos.get(i).getNumeroNodo());
        }
        arbol.setArrayNodos(arrayNodos);
        
    }
    
    /**
     * Método que crea el nuevo automata a partir de follow pos
     * @param arbolSintactico 
     */
    public void crearEstados(SyntaxTree arbolSintactico){
        Automata afd_result = new Automata();
        afd_result.setTipo("AFD DIRECTO");
        
        definirAlfabeto(afd_result, arbolSintactico);
        //el estado inicial se crear a partir del first pos de la raiz
        Estado inicial = new Estado(0);
        TreeSet<Nodo> resultadoInicial = firstPos(arbolSintactico.getRoot());
        afd_result.setEstadoInicial(inicial);
        afd_result.addEstados(inicial);
        
        //variable para marcar los estados ya creados
        ArrayList<ArrayList<TreeSet>> estadosCreados = new ArrayList();
        //se convierte el resultado del firstPos a arrayList
        ArrayList conversionInicial = new ArrayList(resultadoInicial);
        
        estadosCreados.add(conversionInicial);
        
         for (Nodo temp: (ArrayList<Nodo>)conversionInicial){
            if (temp.getId().equals("#"))
                afd_result.addEstadosAceptacion(inicial);
        }
        
        int indexEstadoInicio=0;
        int indexEstados=1;
        //La cola sirve para evaluar los nodos nuevos creados
        Queue<ArrayList> cola = new LinkedList();
        cola.add(conversionInicial);
        
        while(!cola.isEmpty()){
            
            //se evalua el arreglo de nodos
            ArrayList<Nodo> actual = cola.poll();
                
            for (String letra: (HashSet<String>)afd_result.getAlfabeto()){
                
                //arreglo temporal para hacer merge del resultado del followPos
                ArrayList temporal = new ArrayList();
                
                for (Nodo n: actual){
                    if (n.getId().equals(letra)){
                        temporal.addAll((TreeSet<Nodo>) resultadoFollowPos.get(n.getNumeroNodo()));

                    }
                }
                //termina el merge
                
                //si el resultado del merge no existe, se crea un nuevo estao
                if (!estadosCreados.contains(temporal)){

                    Estado siguiente = new Estado(indexEstados);
                    indexEstados++;
                    Estado estadoAnterior = afd_result.getEstados(indexEstadoInicio);
                    
                    estadoAnterior.setTransiciones(new Transicion(estadoAnterior,siguiente,letra));
                    afd_result.addEstados(siguiente);

                    cola.add(temporal);
                    estadosCreados.add(temporal);
                   
                    //verificar si tiene el # que define el estado de aceptacion
                    for (Nodo temp: (ArrayList<Nodo>)temporal){
                        if (temp.getId().equals("#"))
                            afd_result.addEstadosAceptacion(siguiente);
                    }
                }
                else{//si ya existe, se procede a poner transiciones
                   
                    Estado estadoAnterior = afd_result.getEstados(indexEstadoInicio);
                    Estado estadoSiguiente = afd_result.getEstados(estadosCreados.indexOf(temporal));
                    estadoAnterior.setTransiciones(new Transicion(estadoAnterior,estadoSiguiente,letra));
                }

                
            }
            indexEstadoInicio++;
        }
        System.out.println(afd_result);
        this.afdDirecto=afd_result;
        
    }
    
    /**
     * Metodo para mostrar el hash map 
     * en forma de tabla
     */
    private void toStringFollowPos() {
        System.out.println("follow pos");
       
        Iterator it = resultadoFollowPos.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer,Nodo> e = (Map.Entry)it.next();
            System.out.println(e.getKey() + " " + e.getValue());
        }
    }  
   
    /**
     * Minimizacion con algoritmo de Hopcroft de particiones
     * @param AFD
     */
    public void minimizacionAFD(Automata AFD){
        ArrayList<ArrayList<Estado>> particionP = new ArrayList();
        
        /*separar los estados entre los que perteneen al conjunto de estados de aceptacion
        * y los que no, y agregar estos grupos aun conjutno de partició P
        * Ojo: esto significa que la particion P al principio tiene un conjunto con
        * los estados de no acpetacion y otro grupo con los de aceptacion
        */
        ArrayList<Estado> estadosSinAceptacion = new ArrayList();
        for (int i = 0 ; i<AFD.getEstados().size();i++){
            if (!AFD.getEstadosAceptacion().contains(AFD.getEstados().get(i))){
               estadosSinAceptacion.add(AFD.getEstados(i));
            }
        }
        particionP.add(estadosSinAceptacion);
        particionP.add(AFD.getEstadosAceptacion());
        System.out.println(particionP);
      
            
        
       
        int key= 0;
        HashMap<Estado,ArrayList<Integer>> L = new HashMap();
        
        for (int p=0;p<particionP.size();p++){
           ArrayList<Estado> grupoG = particionP.get(p);
            System.out.println(grupoG);
            for (Estado s: grupoG){
                 ArrayList<Integer> Ds = new ArrayList();
                //System.out.println(s.getTransiciones());
                for (String alfabeto: (HashSet<String>)AFD.getAlfabeto()){
                    Estado t = simulador.move(s, alfabeto);
                    
                    for (int j = 0 ;j<particionP.size();j++){
                        
                        
                        if (particionP.get(j).contains(t)){
                            Ds.add(j);
                            
                        }
                        L.put(s, Ds);
                        //System.out.println(Ds + "Ds");
                      
                    }
                }
                
                
                System.out.println(Ds+ " Ds");
            key++;    
            }
             
           // System.out.println(L);
            
            
            /*
            tabla2 = new HashMap();
                    for (Estado e : grupoG) {
                        ArrayList<Integer> alcanzados = tablaDs.get(e);
                        if (tabla2.containsKey(alcanzados))
                            tabla2.get(alcanzados).add(e);
                        else {
                            ArrayList<Estado> tmp = new ArrayList();
                            tmp.add(e);
                            tabla2.put(alcanzados, tmp);
                        }
                    }
            */
            int i = 0;
           
         ArrayList Ki = new ArrayList();
         while (!L.isEmpty()){  
                HashMap<ArrayList<Integer>, ArrayList<Estado>> tabla2 = new HashMap();
                for (Estado e : grupoG) {
                        ArrayList<Integer> alcanzados = L.get(e);
                        if (tabla2.containsKey(alcanzados))
                            tabla2.get(alcanzados).add(e);
                        else {
                            ArrayList<Estado> tmp = new ArrayList();
                            tmp.add(e);
                            tabla2.put(alcanzados, tmp);
                        }
                    }
              
                
             
             
            i++;
                
            
            System.out.println("----");
            System.out.println(particionP);
            System.out.println(Ki);
            System.out.println(grupoG);
            System.out.println("----");
            if (Ki.get(0)!=grupoG){
                particionP.remove(grupoG);
                System.out.println(Ki);
                System.out.println("ki" + Ki.get(1));
               for (int j  =0 ;j<Ki.size();j++){
                   particionP.add((ArrayList<Estado>) Ki.get(j));
               }
                
            }
           
        }
         System.out.println(particionP);
        }
        
    }
    /**
     * Automata de prueba
     * @return Automata
     */
    public Automata automataPrueba(){
        Automata prueba = new Automata();
        
        Estado a = new Estado("a");
        prueba.setEstadoInicial(a);
        prueba.addEstados(a);
        
        Estado b = new Estado("b");
        Estado c = new Estado("c");
        Estado d = new Estado("d");
        Estado e = new Estado("e");
        Estado f = new Estado("f");
        Estado g = new Estado("g");
        Estado h = new Estado("h");
        a.setTransiciones(new Transicion(a,b,"0"));
        a.setTransiciones(new Transicion(a,f,"1"));
        b.setTransiciones(new Transicion(b,c,"1"));
        b.setTransiciones(new Transicion(b,g,"0"));
        c.setTransiciones(new Transicion(c,c,"1"));
        c.setTransiciones(new Transicion(c,a,"0"));
        d.setTransiciones(new Transicion(d,c,"0"));
        d.setTransiciones(new Transicion(d,g,"1"));
        e.setTransiciones(new Transicion(e, f, "1"));
        e.setTransiciones(new Transicion(e, h, "0"));
        f.setTransiciones(new Transicion(f, c, "0"));
        f.setTransiciones(new Transicion(f, g, "1"));
        g.setTransiciones(new Transicion(g,g,"0"));
        g.setTransiciones(new Transicion(g,e,"1"));
        h.setTransiciones(new Transicion(h,c,"1"));
        h.setTransiciones(new Transicion(h,g,"0"));
        prueba.addEstados(b);
        prueba.addEstados(c);
        prueba.addEstados(d);
        prueba.addEstados(e);
        prueba.addEstados(f);
        prueba.addEstados(g);
        prueba.addEstados(h);
        prueba.addEstadosAceptacion(c);
        HashSet alfabeto = new HashSet();
        alfabeto.add("0");
        alfabeto.add("1");
        prueba.setAlfabeto(alfabeto);
        
        return prueba;
    }
    
    /**
     * Método para definir el alfabeto del automata a partir del árbol sináctico
     * @param afd
     * @param arbol 
     */
    public void definirAlfabeto(Automata afd, SyntaxTree arbol){
      HashSet alfabeto = new HashSet();
      String expresion = arbol.getRoot().postOrder();
      for (Character ch: expresion.toCharArray()){
          if (ch!='*'&&ch!='.'&&ch!='|'&&ch!='#'){
              alfabeto.add(Character.toString(ch));
          }
      }
      afd.setAlfabeto(alfabeto);

  }
    
    /**
     * Copiar el alfabeto del AFN al AFD
     * @param afn 
     */
    private void definirAlfabeto(Automata afn){
        this.afd.setAlfabeto(afn.getAlfabeto());
    }
    
    /**
     * Retornar el AFD creado
     * @return Autoamta generado
     */
    public Automata getAfd() {
        return afd;
    }

    public Automata getAfdDirecto(){
        return this.afdDirecto;
    }
    
    public void minimizar (Automata AFD){
        HashMap<Estado,ArrayList<Integer>> tablaDs;
        HashMap<ArrayList<Integer>, ArrayList<Estado>> tabla2;
        /* Conjunto de las particiones del AFD */
        ArrayList<ArrayList<Estado>> particion = new ArrayList();
        
        /* 
        * 1.
        * Separar el AFD en dos grupos, los estados finales y
        * los estados no finales.
        * separar los estados entre los que perteneen al conjunto de estados de aceptacion
        * y los que no, y agregar estos grupos aun conjutno de partició P
        * Ojo: esto significa que la particion P al principio tiene un conjunto con
        * los estados de no acpetacion y otro grupo con los de aceptacion
        */
        ArrayList<Estado> estadosSinAceptacion = new ArrayList();
        for (int i = 0 ; i<AFD.getEstados().size();i++){
            if (!AFD.getEstadosAceptacion().contains(AFD.getEstados().get(i))){
               estadosSinAceptacion.add(AFD.getEstados(i));
            }
        }
        /*agrear los grupos a la particion inicial */
        particion.add(estadosSinAceptacion);
        particion.add(AFD.getEstadosAceptacion());      
        
        /*
         * 2
         * 
         * Construcción de nuevas particiones
         */ 
        ArrayList<ArrayList<Estado>> nuevaParticion;
        while (true) {
            /* Conjunto de nuevas particiones en cada pasada */
            nuevaParticion = new ArrayList();
            /* para cada grupo g en la particion p*/
            for (ArrayList<Estado> grupoG : particion) {
                
                if (grupoG.size() == 1) {
                    /* 
                     * Los grupos unitarios se agregan directamente,
                     * debido a que ya no pueden ser particionados.
                     */
                    nuevaParticion.add(grupoG);
                }
                else {
                    /*
                     * 2.1:
                     * 
                     * Hallamos los grupos alcanzados por
                     * cada estado del grupo actual.
                     */
                    tablaDs = new HashMap();
                    
                    
                    for (Estado s : grupoG)
                    {
                        ArrayList<Integer> gruposAlcanzados = new ArrayList();
                        /*
                        * Para cada símbolo del alfabeto obtenemos el estado
                        * alcanzado por el estado origen y buscamos en qué
                        * grupo de la partición está.
                        */
                        for (String a : (HashSet<String>)AFD.getAlfabeto())
                        {
                            /* Estado destino de la transición */
                            Estado t = simulador.move(s, a);

                            for (int pos=0; pos < particion.size(); pos++) 
                            {
                                ArrayList grupoH = particion.get(pos);

                                if (grupoH.contains(t)) 
                                {
                                    gruposAlcanzados.add(pos);

                                    /* El estado siempre estará en un sólo grupo */
                                    break;
                                }
                            }

                        }
                    
                        
                        tablaDs.put(s, gruposAlcanzados);
                    }
                    /*
                     * 2.2:
                     * 
                     * Calculamos las nuevas particiones
                     */
                    tabla2 = new HashMap();
                    for (Estado e : grupoG) {
                        ArrayList<Integer> alcanzados = tablaDs.get(e);
                        if (tabla2.containsKey(alcanzados))
                            tabla2.get(alcanzados).add(e);
                        else {
                            ArrayList<Estado> tmp = new ArrayList();
                            tmp.add(e);
                            tabla2.put(alcanzados, tmp);
                        }
                    }
                    
                    /*
                     * 2.3:
                     * 
                     * Copiar las nuevas particiones al conjunto de
                     * nuevas particiones.
                     */
                    for (ArrayList<Estado> c : tabla2.values())
                        nuevaParticion.add(c);
                }
            }
            
            
           
            
            /* 
            * 2.4
            * 
            * Si las particiones son iguales, significa que no
            * hubo cambios y debemos terminar. En caso contrario,
            * seguimos particionando.
            */
            if (nuevaParticion.equals(particion))
                break;
            else
                particion = nuevaParticion;
        }
        System.out.println("particiones");
        System.out.println(particion);
    
        
    
    }
       
    
    
    
  

}

