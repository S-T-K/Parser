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
public class TICGrapher extends ApplicationFrame {
JFreeChart chart;
PDFPrinter printer;
    
    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public TICGrapher (PDFPrinter printer,final String title, List<ExtractedMZ> extracteddata, List<ExtractedMZ> extracteddata2, int sizeoffirstset, float lower, float upper) throws DocumentException, FileNotFoundException {

        super(title);

        this.printer=printer;
        
        final XYDataset dataset = createDataset(extracteddata, extracteddata2);
        final JFreeChart chart = createChart(dataset, sizeoffirstset, extracteddata.size()+extracteddata2.size());
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        chart.getXYPlot().getDomainAxis().setRange(lower, upper);
        printer.addChart(chart);

    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset(List<ExtractedMZ> extracteddata, List<ExtractedMZ> extracteddata2) {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        
        
        for (int i = 0; i<extracteddata.size(); i++) {
        
        final XYSeries series = new XYSeries(extracteddata.get(i).getName());
        float maxIntensity = Collections.max(extracteddata.get(i).completeIntensityList);
        
        for (int j = 0; j< extracteddata.get(i).completeRetentionTimeList.size(); j++) {
            
            series.add(extracteddata.get(i).completeRetentionTimeList.get(j)/60,extracteddata.get(i).completeIntensityList.get(j)/maxIntensity);
        }
        dataset.addSeries(series);
        }
     
        for (int i = 0; i<extracteddata2.size(); i++) {
        
        final XYSeries series = new XYSeries(extracteddata2.get(i).getName());
        float maxIntensity = Collections.max(extracteddata2.get(i).completeIntensityList);
        for (int j = 0; j< extracteddata2.get(i).completeRetentionTimeList.size(); j++) {
            
            series.add(extracteddata2.get(i).completeRetentionTimeList.get(j)/60,extracteddata2.get(i).completeIntensityList.get(j)/maxIntensity);
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
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        
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
    
   

}