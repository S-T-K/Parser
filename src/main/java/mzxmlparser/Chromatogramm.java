/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzxmlparser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stefankoch
 */
public class Chromatogramm {
    private List<DataPoint> listofDataPoints;
    private float meanmz;
    
    public Chromatogramm() {
    this.listofDataPoints = new ArrayList<DataPoint>();
    this.meanmz = 0;
    }

   
    public void addDataPoint(DataPoint point) {
       this.meanmz=((this.listofDataPoints.size()*this.meanmz+point.getMasstocharge())/(this.listofDataPoints.size()+1));
       this.listofDataPoints.add(point);
        
    }
    
    
    
    
    /**
     * @return the listofDataPoints
     */
    public List<DataPoint> getListofDataPoints() {
        return listofDataPoints;
    }

    /**
     * @param listofDataPoints the listofDataPoints to set
     */
    public void setListofDataPoints(List<DataPoint> listofDataPoints) {
        this.listofDataPoints = listofDataPoints;
    }

    /**
     * @return the meanmz
     */
    public float getMeanmz() {
        return meanmz;
    }

    /**
     * @param meanmz the meanmz to set
     */
    public void setMeanmz(float meanmz) {
        this.meanmz = meanmz;
    }
    
    
    
}
