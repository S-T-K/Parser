/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzxmlparser;
import java.awt.Color;
import java.util.Collection;
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
import org.jfree.util.ShapeUtilities;
/**
 *
 * @author stefankoch
 */
public class MassGrapher extends ApplicationFrame {
PDFPrinter printer;
    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public MassGrapher (int a, PDFPrinter printer, final String title, List<ExtractedMZ> extracteddata, List<ExtractedMZ> extracteddata2, int sizeoffirstset, float lower, float upper, float mass) {

        super(title);

        this.printer=printer;
        
        final XYDataset dataset = createDataset(a, extracteddata, extracteddata2, mass);
        final JFreeChart chart = createChart(dataset, sizeoffirstset, extracteddata.size()+extracteddata2.size());
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        chart.getXYPlot().getDomainAxis().setRange(lower, upper);
        chart.getXYPlot().getRangeAxis().setRange(-15, 15);
        printer.addChart(chart);
        

    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset(int a, List<ExtractedMZ> extracteddata, List<ExtractedMZ> extracteddata2, float mass) {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        
        
        for (int i = 0; i<extracteddata.size(); i++) {
        
        final XYSeries series = new XYSeries(extracteddata.get(i).getName());
        float maxIntensity = Collections.max(extracteddata.get(i).intensityList.get(a))/20;
        
        
        for (int j = 0; j< extracteddata.get(i).retentionTimeList.get(a).size(); j++) {
            int count = (int) (extracteddata.get(i).intensityList.get(a).get(j)/maxIntensity);
            for (int s = 0; s<= count; s++ ) {
                float deviation = (extracteddata.get(i).massList.get(a).get(j)-mass)/(mass/1000000);
            series.add(extracteddata.get(i).retentionTimeList.get(a).get(j)/60,deviation);
            }
        }
        dataset.addSeries(series);
        }
     
        for (int i = 0; i<extracteddata2.size(); i++) {
        
        final XYSeries series = new XYSeries(extracteddata2.get(i).getName());
        float maxIntensity = Collections.max(extracteddata2.get(i).intensityList.get(a))/20;
        
        for (int j = 0; j< extracteddata2.get(i).retentionTimeList.get(a).size(); j++) {
            int count = (int) (extracteddata2.get(i).intensityList.get(a).get(j)/maxIntensity);
            for (int s = 0; s<= count; s++ ) {
                float deviation = (extracteddata2.get(i).massList.get(a).get(j)-mass)/(mass/1000000);
            series.add(extracteddata2.get(i).retentionTimeList.get(a).get(j)/60,deviation);
            }
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
    private JFreeChart createChart(final XYDataset dataset, int sizeoffirstset, int totalsize) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            super.getTitle(),      // chart title
            "Retention Time [minutes]",                      // x axis label
            "m/z shift [ppm]",                      // y axis label
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
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(false, true);
        
        for (int i =0; i<sizeoffirstset; i++) {
        renderer.setSeriesShapesVisible(i, true);
       renderer.setSeriesPaint(i, new Color(0,0,0,14));
       renderer.setSeriesShape(i, ShapeUtilities.createDiamond(3));
        }
        
        for (int i =sizeoffirstset ; i<totalsize; i++) {
        renderer.setSeriesShapesVisible(i, true);
        renderer.setSeriesPaint(i, new Color(255,0,0,14));
       renderer.setSeriesShape(i, ShapeUtilities.createDiamond(3));
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