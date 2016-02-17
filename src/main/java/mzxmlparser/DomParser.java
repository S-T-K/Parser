// from http://www.java-samples.com/showtutorial.php?tutorialid=152
package mzxmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DomParser {

    //No generics
    List<Scan> listofScans;
    Document mzxmlFile;
    String Filepath;
    B64decoder decoder;

    public DomParser(String path) {
        this.Filepath = path;
        listofScans = new ArrayList<Scan>();
        this.decoder = new B64decoder();
    }

    public List ParseFile() {

        parseXmlFile();
        parseDocument();

        return listofScans;

    }

    private void parseXmlFile() {
        //get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {

            //Using factory get an instance of document builder
            DocumentBuilder db;
            db = dbf.newDocumentBuilder();

            //parse using builder to get DOM representation of the XML file
            mzxmlFile = db.parse(Filepath);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void parseDocument() {
        //get the root elememt
        Element docEle = mzxmlFile.getDocumentElement();

        //get a nodelist of <scan> elements
        NodeList nl = docEle.getElementsByTagName("scan");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {

                //get the scan element
                Element el = (Element) nl.item(i);

                //get the scan object
                Scan e = getScan(el);

                //add it to list
                listofScans.add(e);
            }
        }
    }


    /*
	 * @param EL
	 * @return
     */
    private Scan getScan(Element EL) {

        String numberString = EL.getAttribute("num");
        int number = Integer.parseInt(numberString);

        String peaksCountString = EL.getAttribute("peaksCount");
        int peaksCount = Integer.parseInt(peaksCountString);

        String retentionTimeString = EL.getAttribute("retentionTime");
        retentionTimeString = retentionTimeString.substring(2, (retentionTimeString.length() - 1));
        float retentionTime = Float.parseFloat(retentionTimeString);

        String scanType = EL.getAttribute("scanType");

        // decode B64 encoded peaks, and split them into to arrays containging m/z and corresponding intensity
        String encodedpeaks = getTextValue(EL, "peaks");
        float[] peaks = decoder.extractArray(encodedpeaks);
        float[] massovercharge = new float[peaks.length/2];
        float[] intensity = new float[peaks.length/2];
        for (int i =0; i<peaks.length/2; i++) {
            massovercharge[i]=peaks[i*2];
            intensity[i]=peaks[(i*2)+1];
        }
        
        String msLevelString = EL.getAttribute("msLevel");
        int msLevel = Integer.parseInt(msLevelString);

        char polarity = EL.getAttribute("polarity").charAt(0);

        int msInstrumentID;
        try{
        String msInstrumentIDString = EL.getAttribute("msInstrumentID");
        msInstrumentID = Integer.parseInt(msInstrumentIDString);
        } catch (NumberFormatException e) {
            
            msInstrumentID = 1;
        }
        
        
        Scan e = new Scan(number, peaksCount, retentionTime, massovercharge, intensity, scanType, msLevel, polarity, msInstrumentID);

        return e;
    }

    /**
     * I take a xml element and the tag name, look for the tag and get the text
     * content i.e for <employee><name>John</name></employee> xml snippet if the
     * Element points to employee node and tagName is name I will return John
     *
     * @param ele
     * @param tagName
     * @return
     */
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    }

    /**
     * Calls getTextValue and returns a int value
     *
     * @param ele
     * @param tagName
     * @return
     */
    private int getIntValue(Element ele, String tagName) {
        //in production application you would catch the exception
        return Integer.parseInt(getTextValue(ele, tagName));
    }

    /**
     * Iterate through the list and print the content to console
     */
}
