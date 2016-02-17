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
public class ExtractedMZ {

    private String name;
    private List<Scan> listofScans;
    public List<Float> retentionTimeList = new ArrayList<Float>();
    public List<Float> intensityList = new ArrayList<Float>();
    public List<Float> massList = new ArrayList<Float>();
    public List<Float> completeRetentionTimeList = new ArrayList<Float>();
    public List<Float> completeIntensityList = new ArrayList<Float>();

    public ExtractedMZ(List<Scan> listofScans, String name) {
        this.listofScans = listofScans;
        this.name = name;
    }

    //method to extract desired m/z into 2 arrays with retentiontime and intensity
    public void extract(int msInstrumentID, float desiredMZ, float ppm) {

        float lowerLimit = desiredMZ - (desiredMZ / 1000000 * ppm);
        float upperLimit = desiredMZ + (desiredMZ / 1000000 * ppm);
        boolean found = false;   // check if there is already an intensity for the RT
        for (int i = 0; i < listofScans.size(); i++) {    //for all scans
            found = false;
            if (listofScans.get(i).getMsInstrumentID()==msInstrumentID) {   // only for the desired instrumentID
            for (int j = 0; j < listofScans.get(i).getPeakscount(); j++) {  //for all peaks in scan

                if (listofScans.get(i).getMassovercharge()[j] >= lowerLimit) {   // if mass of peak is greater than lower limit

                    if (listofScans.get(i).getMassovercharge()[j] > upperLimit) {    // and smaller than upper limit
                        if (!found) {   // if not found, add 0 as intensity on the time scale
                            completeRetentionTimeList.add(listofScans.get(i).getRetentionTime());
                            completeIntensityList.add((float) 0.0);
                            //System.out.println("Not found");
                        }
                        break;
                    }
                    retentionTimeList.add(listofScans.get(i).getRetentionTime());   // add retentionTime
                    intensityList.add(listofScans.get(i).getIntensity()[j]);    //add intensity
                    completeRetentionTimeList.add(listofScans.get(i).getRetentionTime());
                    completeIntensityList.add(listofScans.get(i).getIntensity()[j]);
                    found = true;
                    //System.out.println(listofScans.get(i).getRetentionTime() / 60);   //print time in minutes

                }

            }
        }
        }

    }

    public void binaryextract(int msInstrumentID, float desiredMZ, float ppm) {
        float lowerLimit = desiredMZ - (desiredMZ / 1000000 * ppm);
        float upperLimit = desiredMZ + (desiredMZ / 1000000 * ppm);
        int start;
        int end;
        int middle;
        float value;

        for (int i = 0; i < listofScans.size(); i++) {    //for all scans 
            if (listofScans.get(i).getMsInstrumentID()==msInstrumentID) { // only for the desired instrumentID
            start = 0;
            end = listofScans.get(i).getPeakscount() - 1;

            while (start <= end) {
                middle = (int) (start+end)/2;
                value=listofScans.get(i).getMassovercharge()[middle];
                
                if (value <= upperLimit && value >= lowerLimit) {
                    retentionTimeList.add(listofScans.get(i).getRetentionTime());
                    intensityList.add(listofScans.get(i).getIntensity()[middle]);
                    completeRetentionTimeList.add(listofScans.get(i).getRetentionTime());
                    completeIntensityList.add(listofScans.get(i).getIntensity()[middle]);
                    massList.add(listofScans.get(i).getMassovercharge()[middle]);
                
                    break;
                }
                if (value < upperLimit) {
                    start = middle+1;
                } else {
                    end = middle-1;
                }

            } 
            if (start>end){
                completeRetentionTimeList.add(listofScans.get(i).getRetentionTime());
                completeIntensityList.add((float)0.0);
                massList.add((float)0.0);
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
