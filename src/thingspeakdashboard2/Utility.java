/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thingspeakdashboard2;

import com.angryelectron.thingspeak.Feed;
import com.angryelectron.thingspeak.ThingSpeakException;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import static thingspeakdashboard2.FXMLDocumentController.settings;

/**
 *
 * @author Chris
 */
public class Utility {

    public static void setLogLevel(String logLev) {
        Level levLevel = Level.toLevel(logLev);
        Logger root = Logger.getRootLogger();
        root.setLevel(levLevel);
    }

    public static void writeToGraph(LineChart targetGraph, ReadingsObj sensorObj, int measurementID) throws ThingSpeakException, UnirestException {
        Feed feed = sensorObj.getThingFeed();
        int startID = (feed.getChannelLastEntryId() - settings.getSampNum()) + 1;
        DateFormat dateFormat = new SimpleDateFormat("h:mm a");
        XYChart.Series<String, Number> dataSeries = new XYChart.Series();

        for (int i = 0; i < settings.getSampNum(); i++) {
            double tempEntry = Double.parseDouble((String) feed.getEntry(startID + i).getField(measurementID));
            String timeStampStr2 = dateFormat.format(feed.getEntry(startID + i).getCreated());
            dataSeries.getData().add(new XYChart.Data(timeStampStr2, tempEntry));

        }
        sensorObj.getTabName().setText(sensorObj.getTabText());
        targetGraph.getData().addAll(dataSeries);

    }
    

}
