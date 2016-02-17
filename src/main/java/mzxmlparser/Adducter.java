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
public class Adducter {
    List<String[]> listofsubstances;
    List<String[]> listofadducts;
    
    public Adducter (List<String[]> listofsubstances) {
        this.listofsubstances = listofsubstances;
        this.listofadducts = new ArrayList<>();
        
    }
    
    public List<String[]> makeAdducts() {
        
        for (int i =0; i<this.listofsubstances.size(); i++) {
            
            String[] newadduct = new String[3];
            newadduct[0] = "[" + this.listofsubstances.get(i)[0] + "+H]+";
            newadduct[1] = this.listofsubstances.get(i)[1];
            newadduct[2] = String.valueOf(Double.parseDouble(this.listofsubstances.get(i)[2]) + 1.007276);
            this.listofadducts.add(newadduct);
            
            newadduct = new String[3];
            newadduct[0] = "[" + this.listofsubstances.get(i)[0] + "+NH4]+";
            newadduct[1] = this.listofsubstances.get(i)[1];
            newadduct[2] = String.valueOf(Double.parseDouble(this.listofsubstances.get(i)[2]) + 18.033823);
            this.listofadducts.add(newadduct);
            
            newadduct = new String[3];
            newadduct[0] = "[" + this.listofsubstances.get(i)[0] + "+Na]+";
            newadduct[1] = this.listofsubstances.get(i)[1];
            newadduct[2] = String.valueOf(Double.parseDouble(this.listofsubstances.get(i)[2]) + 22.989218);
            this.listofadducts.add(newadduct);
           
            newadduct = new String[3];
            newadduct[0] = "[" + this.listofsubstances.get(i)[0] + "+K]+";
            newadduct[1] = this.listofsubstances.get(i)[1];
            newadduct[2] = String.valueOf(Double.parseDouble(this.listofsubstances.get(i)[2]) + 38.963158);
            this.listofadducts.add(newadduct);
        }
       
        
        return this.listofadducts;
    }
    

}
