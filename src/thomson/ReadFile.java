/**
* Universidad Del Valle de Guatemala
* 11-sep-2015
* Pablo Díaz 13203
*/

package thomson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JFileChooser;

/**
 *
 * @author Pablo
 */
public class ReadFile {
    
     public HashMap leerArchivo(){
      
        int contador=0;
        int tamaño=0;
        String input="";
        BufferedReader br = null;
 
        try {

               
               /* JFileChooser chooser = new JFileChooser();
                int status = chooser.showOpenDialog(null);
                if(status !=JFileChooser.APPROVE_OPTION){
                    System.out.println("\tNo se seleccionó ningún archivo");
                    return null;
                }
                 *///----------------------------------------------------------------------
                File file = new File("cocol.txt");
                br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
                String sCurrentLine;
               
               int cantidadLineas=1;
             
               HashMap<Integer,String> detailString = new HashMap();
               while ((sCurrentLine = br.readLine()) != null) {
                   
                    
                    detailString.put(cantidadLineas, sCurrentLine);
                    
                    if (!sCurrentLine.equals("")){
                        input+=sCurrentLine+"\r\n";
                        cantidadLineas++;
                    }
                
                }
             
                
           // System.out.println("antes input");
            //System.out.println(input);
        
        //System.out.println(detailString);
        //return input;
        return detailString;
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
