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
        FileReader reader = new FileReader("Substanzen.txt");
        List<String[]> allRows = parser.parseAll(reader);
        
        Adducter adducter = new Adducter(allRows);
        List<String[]> allAdducts =  adducter.makeAdducts();
        
        
        //List of all graphers, one for each substance
        List<TICGrapher> graphers = new ArrayList<TICGrapher>();
        PDFPrinter printer = new PDFPrinter();
        
        
        for (int i = 0; i < allAdducts.size(); i++) {
            
            List<ExtractedMZ> listofextracts = new ArrayList<ExtractedMZ>();
            for (int j = 0; j<files.size(); j++) {
                
                DomParser dpe = new DomParser(files.get(j));
                List<Scan> testlist = dpe.ParseFile();
                
                
                float mz = Float.valueOf(allAdducts.get(i)[2]);
                ExtractedMZ testextract = new ExtractedMZ(testlist, files.get(j).toString());
                testextract.binaryextract(1, mz, 10);
                listofextracts.add(testextract);
                
                
            }
            List<ExtractedMZ> listofextracts2 = new ArrayList<ExtractedMZ>();
            for (int j = 0; j<files2.size(); j++) {
                
                DomParser dpe = new DomParser(files2.get(j));
                List<Scan> testlist = dpe.ParseFile();
                
                
                float mz = Float.valueOf(allAdducts.get(i)[2]);
                ExtractedMZ testextract = new ExtractedMZ(testlist, files2.get(j).toString());
                testextract.binaryextract(1, mz, 10);
                listofextracts2.add(testextract);
               
                
            }
            
            TICGrapher demo = new TICGrapher(printer, allAdducts.get(i)[0] + "  " + allAdducts.get(i)[2] + " m/z", listofextracts, listofextracts2, files.size(), Float.valueOf(allAdducts.get(i)[1]) - 2, Float.valueOf(allAdducts.get(i)[1]) + 2);
            demo.pack();
            
            RefineryUtilities.centerFrameOnScreen(demo);
            demo.setVisible(true);
            MassGrapher demo2 = new MassGrapher(printer, allAdducts.get(i)[0] + "  " + allAdducts.get(i)[2] + " m/z", listofextracts, listofextracts2, files.size(), Float.valueOf(allAdducts.get(i)[1]) - 2, Float.valueOf(allAdducts.get(i)[1]) + 2, Float.valueOf(allAdducts.get(i)[2]));
            demo2.pack();
            RefineryUtilities.centerFrameOnScreen(demo2);
            demo2.setVisible(true);
            
            printer.printChart();
            
            
        }
    

    }

}

