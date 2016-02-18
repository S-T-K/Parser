/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mzxmlparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author stefankoch
 */
public class ExtractedMZ {

    private String name;
    private List<Scan> listofScans;
    public List<ArrayList<Float>> retentionTimeList = new ArrayList<ArrayList<Float>>();
    public List<ArrayList<Float>> intensityList = new ArrayList<ArrayList<Float>>();
    public List<ArrayList<Float>> massList = new ArrayList<ArrayList<Float>>();


    public ExtractedMZ(List<Scan> listofScans, String name) {
        this.listofScans = listofScans;
        this.name = name;
    }

    //method to extract desired m/z into 2 arrays with retentiontime and intensity
    public void extract(int msInstrumentID, float[] desiredMZ, float ppm) {
        float[] lowerLimit = new float[desiredMZ.length];
        float[] upperLimit = new float[desiredMZ.length];
        
        for (int i = 0; i<desiredMZ.length; i++) {
        lowerLimit[i] = desiredMZ[i] - (desiredMZ[i] / 1000000 * ppm);
        upperLimit[i] = desiredMZ[i] + (desiredMZ[i] / 1000000 * ppm);
        }
        
        for (int m =0; m<desiredMZ.length; m++) {
            this.retentionTimeList.add(new ArrayList<Float>());
            this.intensityList.add(new ArrayList<Float>());
            this.massList.add(new ArrayList<Float>());
            
        }
        
        
        boolean[] found = new boolean[desiredMZ.length]; 
        Arrays.fill(found, false);


        for (int i = 0; i < listofScans.size(); i++) {    //for all scans
            Arrays.fill(found, false);
            if (listofScans.get(i).getMsInstrumentID()==msInstrumentID) {   // only for the desired instrumentID
                
            for (int j = 0; j < listofScans.get(i).getPeakscount(); j++) {  //for all peaks in scan

                
                for (int m =0; m<desiredMZ.length; m++) {
                    
                    
                if (listofScans.get(i).getMassovercharge()[j] >= lowerLimit[m]) {   // if mass of peak is greater than lower limit

                    if (listofScans.get(i).getMassovercharge()[j] <= upperLimit[m]) {    // and smaller than upper limit
                    retentionTimeList.get(m).add(listofScans.get(i).getRetentionTime());
                    intensityList.get(m).add(listofScans.get(i).getIntensity()[j]);
                    massList.get(m).add(listofScans.get(i).getMassovercharge()[j]);
                    found[m] = true;
                    
                 
                    } else {
 
                        if (!found[m]) {   // if not found, add 0 as intensity on the time scale
                            retentionTimeList.get(m).add(listofScans.get(i).getRetentionTime());
                            
                            intensityList.get(m).add((float) 0.0);
                            massList.get(m).add(0.0f);
                            found[m]=true;
                        }
                    }
                    }
                    
                    
                    

                }

            }
            }
        }
        }

    

    

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
