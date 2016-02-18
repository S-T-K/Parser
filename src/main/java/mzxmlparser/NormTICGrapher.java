/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzxmlparser;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
/**
 *
 * @author stefankoch
 */
public class NormTICGrapher extends ApplicationFrame {
JFreeChart chart;
PDFPrinter printer;
    
    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public NormTICGrapher (int a, PDFPrinter printer,final String title, List<ExtractedMZ> extracteddata, List<ExtractedMZ> extracteddata2, int sizeoffirstset, float lower, float upper) throws DocumentException, FileNotFoundException {

        super(title);

        this.printer=printer;
        
        final XYDataset dataset = createDataset(a, extracteddata, extracteddata2, lower, upper);
        final JFreeChart chart = createChart(dataset, sizeoffirstset, extracteddata.size()+extracteddata2.size());
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        chart.getXYPlot().getDomainAxis().setRange(lower, upper);
        chart.getXYPlot().getRangeAxis().setRange(0, 1);
        printer.addChart(chart);

    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset(int a, List<ExtractedMZ> extracteddata, List<ExtractedMZ> extracteddata2, float lower, float upper) {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        
        
   
        
        
        for (int i = 0; i<extracteddata.size(); i++) {
        
        final XYSeries series = new XYSeries(extracteddata.get(i).getName());
        
        
        float maxIntensity = getMax(extracteddata.get(i).retentionTimeList.get(a), extracteddata.get(i).intensityList.get(a), lower, upper);
        float max = 0;
        
        for (int j = 0; j< extracteddata.get(i).retentionTimeList.get(a).size(); j++) {
            max = extracteddata.get(i).intensityList.get(a).get(j);
            while (j<extracteddata.get(i).retentionTimeList.get(a).size()-1 && Math.abs(extracteddata.get(i).retentionTimeList.get(a).get(j) - extracteddata.get(i).retentionTimeList.get(a).get(j+1)) < 0.01) {
                if (extracteddata.get(i).intensityList.get(a).get(j) > max) {
                    max = extracteddata.get(i).intensityList.get(a).get(j+1);
                }
                j++;
                
            }
          
            
            series.add(extracteddata.get(i).retentionTimeList.get(a).get(j)/60,max/maxIntensity); 
        }
        dataset.addSeries(series);
        }
     
        for (int i = 0; i<extracteddata2.size(); i++) {
        
        final XYSeries series = new XYSeries(extracteddata2.get(i).getName());
        float maxIntensity = getMax(extracteddata2.get(i).retentionTimeList.get(a), extracteddata2.get(i).intensityList.get(a), lower, upper);
        float max = 0;
        
        for (int j = 0; j< extracteddata2.get(i).retentionTimeList.get(a).size(); j++) {
            max = extracteddata2.get(i).intensityList.get(a).get(j);
            while (j<(extracteddata2.get(i).retentionTimeList.get(a).size()-1) && Math.abs(extracteddata2.get(i).retentionTimeList.get(a).get(j) - extracteddata2.get(i).retentionTimeList.get(a).get(j+1)) < 0.01) {
                if (extracteddata2.get(i).intensityList.get(a).get(j) > max) {
                    max = extracteddata2.get(i).intensityList.get(a).get(j+1);
                }
                j++;
                
            }
            series.add(extracteddata2.get(i).retentionTimeList.get(a).get(j)/60,max/maxIntensity); 
        }
        dataset.addSeries(series);
        }

        
        
      
                
        return dataset;
        
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset, int sizeoffirstset, int totalsize) throws DocumentException, FileNotFoundException {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            super.getTitle(),      // chart title
            "Retention Time [minutes]",                      // x axis label
            "Intensity",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            false,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
        for (int i =0; i<sizeoffirstset; i++) {
        renderer.setSeriesShapesVisible(i, false);
       renderer.setSeriesPaint(i, Color.BLACK); 
        }
        
        for (int i =sizeoffirstset ; i<totalsize; i++) {
        renderer.setSeriesShapesVisible(i, false);
       renderer.setSeriesPaint(i, Color.RED); 
        }
       
       
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        
        
        
        
        
        return chart;
        
    }
    
    private float getMax (List<Float> retentionTimeList, List<Float> intensityList, float lower, float upper) {
        
       float max = 1; 
       lower = lower*60;
       upper = upper*60;
        
       for (int i =0; i<retentionTimeList.size(); i++) {
          
           if (retentionTimeList.get(i) <= upper && retentionTimeList.get(i) >= lower) {
               if (intensityList.get(i)> max) {
                   max = intensityList.get(i);
               }
           } else {
           }
           
       }
      
        return max;
    }
   

}