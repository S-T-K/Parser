/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzxmlparser;

import com.itextpdf.text.DocumentException;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.jfree.ui.RefineryUtilities;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 *
 * @author azadmin
 */
public class MzXMLparser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, DocumentException {

        ArrayList<String> files = new ArrayList<String>();
        Files.walk(Paths.get("C:/Users/stefankoch/Documents/Daten/Vergleich/neu")).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                
                files.add(filePath.toString());
            }
        });
        
        
        ArrayList<String> files2 = new ArrayList<String>();
        Files.walk(Paths.get("C:/Users/stefankoch/Documents/Daten/Vergleich/alt")).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                
                files2.add(filePath.toString());
            }
        });
        
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);
        FileReader reader = new FileReader("Substanzen2.txt");
        List<String[]> allRows = parser.parseAll(reader);
        
        Adducter adducter = new Adducter(allRows);
        List<String[]> allAdducts =  adducter.makeAdducts();
        
        for (int i =0; i<allAdducts.size(); i++) {
            System.out.println(Arrays.toString(allAdducts.get(i)));
            
        }
        
        
        
        float[] desiredMZ = new float[allAdducts.size()];
        for (int i=0; i<allAdducts.size(); i++) {
            desiredMZ[i]=Float.parseFloat(allAdducts.get(i)[2]);
            
        }
        
        
        
        
        
        
        
        
        //List of all graphers, one for each substance
        List<NormTICGrapher> graphers = new ArrayList<NormTICGrapher>();
        PDFPrinter printer = new PDFPrinter();
        
        
        
        List<ExtractedMZ> listofextracts1 = new ArrayList<ExtractedMZ>();
        List<ExtractedMZ> listofextracts2 = new ArrayList<ExtractedMZ>();
        
        
        for (int f = 0; f<files.size(); f++) {
                DomParser dpe = new DomParser(files.get(f));
                List<Scan> testlist = dpe.ParseFile();
   
                ExtractedMZ testextract = new ExtractedMZ(testlist, files.get(f).toString());
                testextract.extract(1, desiredMZ, 7);
                listofextracts1.add(testextract);
                System.out.println("Phase 1: " + ((double)f/(double)files.size())*100+ "%") ;
            
        }
        
        for (int f = 0; f<files2.size(); f++) {
                DomParser dpe = new DomParser(files2.get(f));
                List<Scan> testlist = dpe.ParseFile();
   
                ExtractedMZ testextract = new ExtractedMZ(testlist, files2.get(f).toString());
                testextract.extract(1, desiredMZ, 7);
                listofextracts2.add(testextract);
             System.out.println("Phase 2: " + ((double)f/(double)files2.size())*100+ "%") ;
        }
        
       
        
        for (int a = 0; a<allAdducts.size(); a++) {
            
            TICGrapher demo1 = new TICGrapher(a,printer, allAdducts.get(a)[0] + "  " + allAdducts.get(a)[2] + " m/z", listofextracts1, listofextracts2, files.size(), Float.valueOf(allAdducts.get(a)[1]) - 2, Float.valueOf(allAdducts.get(a)[1]) + 2);
          
            
            NormTICGrapher demo = new NormTICGrapher(a,printer, allAdducts.get(a)[0] + "  " + allAdducts.get(a)[2] + " m/z", listofextracts1, listofextracts2, files.size(), Float.valueOf(allAdducts.get(a)[1]) - 2, Float.valueOf(allAdducts.get(a)[1]) + 2);
            
            //demo.setVisible(true);
            
            
            MassGrapher demo2 = new MassGrapher(a, printer, allAdducts.get(a)[0] + "  " + allAdducts.get(a)[2] + " m/z", listofextracts1, listofextracts2, files.size(), Float.valueOf(allAdducts.get(a)[1]) - 2, Float.valueOf(allAdducts.get(a)[1]) + 2, Float.valueOf(allAdducts.get(a)[2]));
            
            //demo2.setVisible(true);
            
           
           
            
            
             System.out.println("Phase 3: " + ((double)a/(double)allAdducts.size())*100+ "%") ;
        }
         printer.setFilesblack(files);
         printer.setFilesred(files2);
            
         printer.printChart();
    System.out.println("Done");
    }
    }


