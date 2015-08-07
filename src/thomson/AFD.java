package thomson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

/**
* Universidad Del Valle de Guatemala
* 07-ago-2015
* Pablo Díaz 13203
*/

/**
 *
 * @author Pablo
 */
public class AFD<T> {

    private HashSet<String> alfabeto;
    private Estado inicial;
    private ArrayList<Estado> estados;
    private ArrayList<Estado> aceptacion;

    public AFD() {
        this.alfabeto = new HashSet();
        this.estados =  new ArrayList();
        this.aceptacion =  new ArrayList();
    }

    public HashSet<String> getAlfabeto() {
        return alfabeto;
    }

    public void addtAlfabeto(String alfabeto) {
        this.alfabeto.add(alfabeto);
    }

    public Estado getEstadoInicial() {
        return inicial;
    }

    public void setEstadoInicial(Estado inicial) {
        this.inicial = inicial;
    }

    public ArrayList<Estado> getEstados() {
        return estados;
    }

    public void addEstados(Estado estado) {
        this.estados.add(estado);
    }
    public void setEstados(ArrayList<Estado> estados) {
        this.estados = estados;
    }

    public ArrayList<Estado> getEstadosAceptacion() {
        return aceptacion;
    }

    public void addEstadosAceptacion(Estado aceptacion) {
        this.aceptacion.add(aceptacion);
    }
  
}
