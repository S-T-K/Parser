/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzxmlparser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author stefankoch
 */
public class PDFPrinter {

    List<JFreeChart> listofcharts;
    private ArrayList<String> filesblack;
    private ArrayList<String> filesred;

    public PDFPrinter() {
        this.listofcharts = new ArrayList<JFreeChart>();

    }

    public void addChart(JFreeChart newchart) {
        this.listofcharts.add(newchart);

    }

    public void printChart() throws FileNotFoundException, DocumentException {
        Document doc = new Document();
        FileOutputStream baosPDF = new FileOutputStream("test.pdf");
        PdfWriter docWriter = null;
        docWriter = PdfWriter.getInstance(doc, baosPDF);

        doc.addProducer();
        doc.addCreator(this.getClass().getName());
        doc.addTitle("jfreechart pdf");
        doc.setPageSize(PageSize.A0);

        doc.open();

        int width = 750;
        int height = 500;
        int pageheight = 3334;

        // get the direct pdf content
        PdfContentByte dc = docWriter.getDirectContent();

        // get a pdf template from the direct content
        int numberofrowsperpage = 0;
        for (int i = 0; i < this.listofcharts.size(); i = i + 3) {

            PdfTemplate tp1 = dc.createTemplate(width, height);
            // create an AWT renderer from the pdf template
            Graphics2D g21 = tp1.createGraphics(width, height, new DefaultFontMapper());
            Rectangle2D r2D1 = new Rectangle2D.Double(0, 0, width, height);
            this.listofcharts.get(i).draw(g21, r2D1, null);
            g21.dispose();

            // add the rendered pdf template to the direct content
            // you will have to play around with this because the chart is absolutely positioned.
            // 38 is just a typical left margin
            // docWriter.getVerticalPosition(true) will approximate the position that the content above the chart ended
            dc.addTemplate(tp1, 38, pageheight - height * (numberofrowsperpage + 1));

            PdfTemplate tp = dc.createTemplate(width, height);
            // create an AWT renderer from the pdf template
            Graphics2D g2 = tp.createGraphics(width, height, new DefaultFontMapper());
            Rectangle2D r2D = new Rectangle2D.Double(0, 0, width, height);
            this.listofcharts.get(i + 1).draw(g2, r2D, null);
            g2.dispose();

            // add the rendered pdf template to the direct content
            // you will have to play around with this because the chart is absolutely positioned.
            // 38 is just a typical left margin
            // docWriter.getVerticalPosition(true) will approximate the position that the content above the chart ended
            dc.addTemplate(tp, 760, pageheight - height * (numberofrowsperpage + 1));

            PdfTemplate tp2 = dc.createTemplate(width, height);
            // create an AWT renderer from the pdf template
            Graphics2D g22 = tp2.createGraphics(width, height, new DefaultFontMapper());
            Rectangle2D r2D2 = new Rectangle2D.Double(0, 0, width, height);
            this.listofcharts.get(i + 2).draw(g22, r2D2, null);
            g22.dispose();

            // add the rendered pdf template to the direct content
            // you will have to play around with this because the chart is absolutely positioned.
            // 38 is just a typical left margin
            // docWriter.getVerticalPosition(true) will approximate the position that the content above the chart ended
            dc.addTemplate(tp2, 1510, pageheight - height * (numberofrowsperpage + 1));

            numberofrowsperpage++;

            if ((i + 3) % 12 == 0) {
                doc.newPage();
                numberofrowsperpage = 0;

            }

        }

        Font catFont1 = new Font(Font.FontFamily.TIMES_ROMAN, 30,
                Font.BOLD);
        Font catFont2 = new Font(Font.FontFamily.TIMES_ROMAN, 30,
                Font.BOLD);
        catFont2.setColor(BaseColor.RED);
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        
        for (int i =0; i<this.filesblack.size(); i++) {
        preface.add(new Paragraph(this.filesblack.get(i), catFont1));
        addEmptyLine(preface, 1);}
        
        addEmptyLine(preface, 4);
        
        for (int i =0; i<this.filesred.size(); i++) {
        preface.add(new Paragraph(this.filesred.get(i), catFont2));
        addEmptyLine(preface, 1);}
        
        doc.add(preface);

        doc.close();

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }

    }

    /**
     * @return the filesblack
     */
    public ArrayList<String> getFilesblack() {
        return filesblack;
    }

    /**
     * @param filesblack the filesblack to set
     */
    public void setFilesblack(ArrayList<String> filesblack) {
        this.filesblack = filesblack;
    }

    /**
     * @return the filesred
     */
    public ArrayList<String> getFilesred() {
        return filesred;
    }

    /**
     * @param filesred the filesred to set
     */
    public void setFilesred(ArrayList<String> filesred) {
        this.filesred = filesred;
    }
}
