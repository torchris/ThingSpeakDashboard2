/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thingspeakdashboard2;

import com.angryelectron.thingspeak.Channel;
import com.angryelectron.thingspeak.Entry;
import com.angryelectron.thingspeak.Feed;
import com.angryelectron.thingspeak.FeedParameters;
import com.angryelectron.thingspeak.ThingSpeakException;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Tab;
import static thingspeakdashboard2.FXMLDocumentController.settings;

/**
 *Readings Object includes all the settings needed to generate a feed return object that generates the graphs.
 * @author Chris
 */
public class ReadingsObj {

    static private int chanID;
    static private String readAPI;
//    static private int sampleNum;
    private int tempID;
    private int humdID;
    private Tab tabName;
    private String tabText;
    private LineChart tempGraphName;
    private LineChart humdGraphName;
    private Feed thingFeed;
    
    /**
     * The prototype of ReadingsObj includes information necessary to create the graph - both info to be fed into ThingSpeak and related to the graphs.
     * 
     * @param chanID integer value of channel ID for ThingSpeak
     * @param readAPI string value of Read API obtained from ThingSpeak.
     * @param tempID integer value of the ThinhgSpeak field associated with temperature readings.
     * @param humdID integer value of the ThinhgSpeak field associated with humidity readings.
     * @param tabName JavaFX Tab object name *
     * @param tabText string name to actually appear in the tab.
     * @param tempGraphName JavaFX LineChart object name for the temp graph *
     * @param humdGraphName JavaFX LineChart object name for the humidity graph. *
     */

    public ReadingsObj(int chanID, String readAPI, int tempID, int humdID, Tab tabName, String tabText, LineChart tempGraphName, LineChart humdGraphName) {
        this.chanID = chanID;
        this.readAPI = readAPI;
        this.tempID = tempID;
        this.humdID = humdID;
        this.tabName = tabName;
        this.tabText = tabText;
        this.tempGraphName = tempGraphName;
        this.humdGraphName = humdGraphName;
        this.thingFeed = thingFeed;
    }

    /**
     * Sets the ThingSpeak channel ID
     * @param chanID integer channel ID in the ThingSpeak interface
     */   
    public void setchanID(int chanID) {
        this.chanID = chanID;
    }

    /**
     * Gets the ThingSpeak channel ID number from the object.
     * @return integer of the channel ID on ThingSpeak.
     */
    public int getchanID() {
        return chanID;
    }

    /**
     * Read API for the channel referenced by the chanID field.
     * @param readAPI String value for the Read API from the ThingSpeak interface.
     */
    
    public void setreadAPI(String readAPI) {
        this.readAPI = readAPI;
    }

    /**
     * 
     * @return 
     */
    
    public String getreadAPI() {
        return readAPI;
    }

/**
 * 
 * @param tempID 
 */
    public void settempID(int tempID) {
        this.tempID = tempID;
    }

    /**
     * 
     * @return 
     */
    
    public int gettempID() {
        return tempID;
    }

    /**
     * 
     * @param humdID 
     */
    
    public void sethumdID(int humdID) {
        this.humdID = humdID;
    }

    /**
     * 
     * @return 
     */
    
    public int gethumdID() {
        return humdID;
    }

    /**
     * 
     * @param tabName 
     */
    
    public void setTabName(Tab tabName) {
        this.tabName = tabName;
    }
    
    /**
     * 
     * @return 
     */

    public Tab getTabName() {
        return tabName;
    }

    /**
     * 
     * @param tabText 
     */
    
    public void setTabText(String tabText) {
        this.tabText = tabText;
    }
    
    /**
     * 
     * @return 
     */

    public String getTabText() {
        return tabText;
    }

    /**
     * 
     * @param tempGraphName 
     */
    public void setTempGraphName(LineChart tempGraphName) {
        this.tempGraphName = tempGraphName;
    }

    /**
     * 
     * @return 
     */
    public LineChart getTempGraphName() {
        return tempGraphName;
    }
/**
 * 
 * @param humdGraphName 
 */
    public void setHumdGraphName(LineChart humdGraphName) {
        this.humdGraphName = humdGraphName;
    }
/**
 * 
 * @return 
 */
    public LineChart getHumdGraphName() {
        return humdGraphName;
    }
/**
 * 
 * @return
 * @throws UnirestException
 * @throws ThingSpeakException 
 */
    public Feed getThingFeed() throws UnirestException, ThingSpeakException  {

        Channel sensorChannel = new Channel(chanID, readAPI);
        FeedParameters options = new FeedParameters();
        options.results(settings.getSampNum());
        Feed feed = sensorChannel.getChannelFeed(options);
        Entry feedEntry = feed.getEntry(feed.getChannelLastEntryId());
        return feed;
    }
}



