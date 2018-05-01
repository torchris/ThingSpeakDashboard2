/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thingspeakdashboard2;

/**
 *The Settings Object is used in the dashboard to capture the settings of the dashboard which are loaded from the properties file.
 * These include the program defaults and the information used to build the feed objects.
 * @author Chris
 */
public class SettingsObj {
    
    	/** 
         * Captures the dashboard settings
	 */

    static private int sampNum;
    static private int refreshTime;
    static private String logLev;
    
    /**
     * Default constructor for SettingsObj object
     * @param sampNum integer value of number of samples the feed object is supposed to retrieve.
     * @param refreshTime integer value of refresh time in milliseconds.
     */

    public SettingsObj(int sampNum, int refreshTime, String logLev) {
        this.sampNum = sampNum;
        this.refreshTime = refreshTime;
        this.logLev = logLev;
    }
    
    /**
     * Sets the number of samples to be retrieved from the feed.
     * Probably best to keep in a range under about 300.
     * @param sampNum integer number of samples.
     */

    public void setSampNum(int sampNum) {
        this.sampNum = sampNum;
    }

    /**
     * Get the number of samples.
     * @return integer value of samples.
     */
    public int getSampNum() {
        return sampNum;
    }
    
    /**
     * Sets the interval for refreshes in milliseconds.
     * @param refreshTime integer milliseconds between refreshes.
     */

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }
    
    /**
     * Returns the refresh interval in milliseconds
     * @return integer value of refresh interval.
     */

    public int getRefreshTime() {
        return refreshTime;
    }

        public void setLogLevel(String logLev) {
        this.logLev = logLev;
    }
    
    /**
     * Returns the refresh interval in milliseconds
     * @return integer value of refresh interval.
     */

    public String getLogLevel() {
        return logLev;
    }
}
