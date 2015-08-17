/**
* Universidad Del Valle de Guatemala
* 04-ago-2015
* Pablo Díaz 13203
*/

package thomson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Pablo
 */
public class FileCreator {
    
    public FileCreator(){
        
    }
     
    public void crearArchivo(String output, double tiempoCreacion, double tiempoSimulacion,String tipoAutomata){
        try {
            
                
                //output += "\r\n"+"\r\n"+"\r\n"+leerArchivo();
                File file;
                File dummy = new File("");
                String path = dummy.getAbsolutePath();
                String nombreArchivo;
                if (tipoAutomata.equals("AFN"))
                   nombreArchivo="AFN.txt";
                else if(tipoAutomata.equals("AFD"))
                   nombreArchivo="AFD_Subconjuntos.txt";
                else
                    nombreArchivo="AFD_Directo.txt";
                file = new File(nombreArchivo);
                // if FileCreator doesnt exists, then create it
                System.out.println(file.getAbsoluteFile());
                
               
                FileWriter fw = new FileWriter(path+"/GeneracionAutomatas/TXT/"+nombreArchivo);
                BufferedWriter bw = new BufferedWriter(fw);
               
                bw.write(output+"\r\n");
                bw.write("Tiempo Creación: "+tiempoCreacion+" ms"+"\r\n");
                bw.write("Tiempo Simulacion: " + tiempoSimulacion+" ms"+"\r\n");

                bw.close();

                System.out.println("Se ha creado el archivo exitosamente");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String leerArchivo(){
      
        int contador=0;
        int tamaño=0;
        String input=" ";
        BufferedReader br = null;
 
        try {

                String sCurrentLine;
                File file = new File("Automata.txt");
                br = new BufferedReader(new FileReader(file.getAbsoluteFile()));

               
               
               while ((sCurrentLine = br.readLine()) != null) {
                    
                    input+=sCurrentLine+"\r\n";
                
                }
             
                
        input+="\r\n";
                
        return input;
        } catch (IOException e) {
               
        } finally {
                try {
                        if (br != null)br.close();
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
        }
        return null;
        
    }

}
