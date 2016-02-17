/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzxmlparser;

/**
 *
 * @author stefankoch
 */
public class DataPoint {
    private float masstocharge;
    private float retentiontime;
    private float intensity;
    private float scannumber;
    
    
    public DataPoint() {}
            
    public DataPoint(float masstocharge, float retentiontime, float intensity, float scannumber) {
    this.masstocharge=masstocharge;
    this.retentiontime = retentiontime;
    this.intensity=intensity;
    this.scannumber=scannumber;
    }

    
    
    
    
    
    /**
     * @return the masstocharge
     */
    public float getMasstocharge() {
        return masstocharge;
    }

    /**
     * @param masstocharge the masstocharge to set
     */
    public void setMasstocharge(float masstocharge) {
        this.masstocharge = masstocharge;
    }

    /**
     * @return the retentiontime
     */
    public float getRetentiontime() {
        return retentiontime;
    }

    /**
     * @param retentiontime the retentiontime to set
     */
    public void setRetentiontime(float retentiontime) {
        this.retentiontime = retentiontime;
    }

    /**
     * @return the intensity
     */
    public float getIntensity() {
        return intensity;
    }

    /**
     * @param intensity the intensity to set
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    /**
     * @return the scannumber
     */
    public float getScannumber() {
        return scannumber;
    }

    /**
     * @param scannumber the scannumber to set
     */
    public void setScannumber(float scannumber) {
        this.scannumber = scannumber;
    }
    
    
    
}
