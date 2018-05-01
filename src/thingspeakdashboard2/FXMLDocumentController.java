/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thingspeakdashboard2;

//========Imports=========//
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import com.angryelectron.thingspeak.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import org.apache.log4j.Logger;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.apache.log4j.BasicConfigurator;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;
import javafx.scene.control.ChoiceBox;
import eu.hansolo.medusa.Gauge;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;


/**
 *
 * @author Chris
 */
public class FXMLDocumentController implements Initializable {

    public static SettingsObj settings = new SettingsObj(125, 120000, "INFO");
    Preferences pref = Preferences.userNodeForPackage(FXMLDocumentController.class);
    Logger log = Logger.getLogger(FXMLDocumentController.class.getName());
   


    //============= Methods that do stuff ==================//
    public void GetPrefs() throws ThingSpeakException, UnirestException, FileNotFoundException {

        try (InputStream is = new BufferedInputStream(new FileInputStream("preferences.xml"))) {
            try {
                pref.importPreferences(is);
            } catch (InvalidPreferencesFormatException ex) {
                log.log(Priority.ERROR, ex);
            }
        } catch (IOException ex) {
            log.log(Priority.ERROR, ex);
        }

        settings.setRefreshTime(Integer.parseInt(pref.get("refreshtime", "180000")));
        settings.setSampNum(Integer.parseInt(pref.get("samplenumber", "120")));
        settings.setLogLevel(pref.get("defaultLogLevel", "INFO"));
        sampleNumField.setText(Integer.toString(settings.getSampNum()));
        settingsRefreshTimeField.setText(Integer.toString(settings.getRefreshTime()));
        settingsChanIDFieldTabA.setText(pref.get("sensor1_Chan_ID", "1234"));
        settingsReadAPIField.setText(pref.get("sensor1_ReadAPI", "xxxxxx"));
        System.out.println(pref.get("sensor1_ReadAPI", "xxxxxx"));
        settingsTabNameField.setText(pref.get("sensor1_Tab_Text", "1234"));
        settingsTempFieldA.setValue(pref.get("sensor1_Temp_Field", "1234"));
        settingsHumidityFieldA.setValue(pref.get("sensor1_Humd_Field", "1234"));
        settingsChanIDFieldTabB.setText(pref.get("sensor2_Chan_ID", "1234"));
        settingsReadAPIFieldB.setText(pref.get("sensor2_ReadAPI", "1234"));
        settingsTabNameFieldB.setText(pref.get("sensor2_Tab_Text", "1234"));
        settingsTempFieldB.setValue(pref.get("sensor2_Temp_Field", "1234"));
        settingsHumidityFieldB.setValue(pref.get("sensor2_Humd_Field", "1234"));
        settingsLogLeveldrop.setValue(settings.getLogLevel());
        setLogLevel(settings.getLogLevel());

        ReadingsObj tabA = new ReadingsObj(
                Integer.parseInt(pref.get("sensor1_Chan_ID", "1234")),
                pref.get("sensor1_ReadAPI", "xxxxxx"),
                Integer.parseInt(pref.get("sensor1_Temp_Field", "1234")),
                Integer.parseInt(pref.get("sensor1_Humd_Field", "1234")),
                aTab,
                pref.get("sensor1_Tab_Text", "xxxxxx"),
                tempGrapha, humidityGrapha);

        writeToGraph(tabA.getTempGraphName(), tabA, tabA.gettempID());
        writeToGraph(tabA.getHumdGraphName(), tabA, tabA.gethumdID());
        Feed tabAfeed = tabA.getThingFeed();
        Sensor1TempGauge.setValue(Double.parseDouble((String) tabAfeed.getChannelLastEntry().getField(tabA.gettempID())));
        Sensor1HumdGauge.setValue(Double.parseDouble((String) tabAfeed.getChannelLastEntry().getField(tabA.gethumdID())));
        Sensor1SummaryLabel.setText(tabA.getTabText());

        ReadingsObj tabB = new ReadingsObj(
                Integer.parseInt(pref.get("sensor2_Chan_ID", "1234")),
                pref.get("sensor2_ReadAPI", "xxxxxx"),
                Integer.parseInt(pref.get("sensor2_Temp_Field", "1234")),
                Integer.parseInt(pref.get("sensor2_Humd_Field", "1234")),
                bTab,
                pref.get("sensor2_Tab_Text", "xxxxxx"),
                tempGraphb, humidityGraphb);

        writeToGraph(tabB.getTempGraphName(), tabB, tabB.gettempID());
        writeToGraph(tabB.getHumdGraphName(), tabB, tabB.gethumdID());
        Feed tabBfeed = tabB.getThingFeed();
        Sensor2TempGauge.setValue(Double.parseDouble((String) tabBfeed.getChannelLastEntry().getField(tabB.gettempID())));
        Sensor2HumdGauge.setValue(Double.parseDouble((String) tabBfeed.getChannelLastEntry().getField(tabB.gethumdID())));
        Sensor2SummaryLabel.setText(tabB.getTabText());
    }

    public void SetPrefs(
            String refreshTime,
            String sampleNumber,
            String sensor1ChanID,
            String sensor1ReadAPI,
            String tabAText,
            String sensor1TempField,
            String sensor1HumdField,
            String sensor2ChanID,
            String sensor2ReadAPI,
            String tabBText,
            String sensor2TempField,
            String sensor2HumdField,
            String logLevel) throws ThingSpeakException, UnirestException, FileNotFoundException {

        try {
            pref.put("refreshtime", refreshTime);
            pref.put("samplenumber", sampleNumber);
            pref.put("sensor1_Chan_ID", sensor1ChanID);
            pref.put("sensor1_ReadAPI", sensor1ReadAPI);
            pref.put("sensor1_Tab_Text", tabAText);
            pref.put("sensor1_Temp_Field", sensor1TempField);
            pref.put("sensor1_Humd_Field", sensor1HumdField);
            pref.put("sensor2_Chan_ID", sensor2ChanID);
            pref.put("sensor2_ReadAPI", sensor2ReadAPI);
            pref.put("sensor2_Tab_Text", tabBText);
            pref.put("sensor2_Temp_Field", sensor2TempField);
            pref.put("sensor2_Humd_Field", sensor2HumdField);
            pref.put("defaultLogLevel", logLevel);
            pref.exportSubtree(new BufferedOutputStream(new FileOutputStream("preferences.xml")));
        } catch (IOException ex) {
            log.log(Priority.ERROR, ex);
        } catch (BackingStoreException ex) {
            log.log(Priority.ERROR, ex);
        }

    }

    Task<Integer> runTask;

    public void runTaskThing() throws ThingSpeakException, UnirestException {
        this.runTask = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                int iterations;
                iterations = 0;

                while (!runTask.isCancelled()) {
                    iterations++;

                    if (isCancelled()) {
                        updateMessage("Cancelled");
                        break;
                    }
                    updateMessage("Refresh " + iterations);
                    updateProgress(iterations, 1000);
                    System.out.println("Loop num: " + iterations);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tempGrapha.getData().clear();
                                humidityGrapha.getData().clear();
                                tempGraphb.getData().clear();
                                humidityGraphb.getData().clear();
                                try {
                                    GetPrefs();
                                } catch (FileNotFoundException ex) {
                                    log.log(Priority.ERROR, ex);
                                }
                                //graphData(garage);
                                RefreshNum.setText(runTask.messageProperty().getValue());
                                System.out.println("Looping!");
                            } catch (ThingSpeakException | UnirestException ex) {
                                log.log(Priority.ERROR, ex);

                            }
                        }
                    });

                    Thread.sleep(settings.getRefreshTime());

                }
                return iterations;
            }
        };

        Thread th = new Thread(runTask);
        th.setDaemon(true);
        th.start();

    }

    ;
    
    public void writeToGraph(LineChart targetGraph, ReadingsObj sensorObj, int measurementID) throws ThingSpeakException, UnirestException {
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
    
    public void setLogLevel(String logLev){
        Level levLevel = Level.toLevel(logLev);
                     Logger root = Logger.getRootLogger();
     root.setLevel(levLevel);
    }

//==============Interface action handlers================//
    @FXML
    private void handleAutoRefresh(ActionEvent event) throws ThingSpeakException, UnirestException {
        stopAutoRefreshMenuItem.setDisable(false);
        autoRefreshMenuItem.setDisable(true);
        RefreshNum.setVisible(true);
        RefreshNumLabel.setVisible(true);
        tempGrapha.getData().clear();
        tempGraphb.getData().clear();
        humidityGrapha.getData().clear();
        humidityGrapha.getData().clear();
        runTaskThing();
        System.out.println("Auto Refreshing!");
    }

    @FXML
    private void handleRefreshOnceAction(ActionEvent event) throws ThingSpeakException, UnirestException {
        try {
            tempGrapha.getData().clear();
            tempGraphb.getData().clear();
            humidityGrapha.getData().clear();
            humidityGrapha.getData().clear();
            GetPrefs();
            System.out.println("You clicked me!");
        } catch (FileNotFoundException ex) {
            log.log(Priority.ERROR, ex);
        }
    }

    @FXML
    private void handleExit(ActionEvent event) throws ThingSpeakException, UnirestException {
        System.out.println("Good bye!");
        Platform.exit();
    }

    @FXML
    private void handleStopRefresh(ActionEvent event) throws ThingSpeakException, UnirestException {
        autoRefreshMenuItem.setDisable(false);
        stopAutoRefreshMenuItem.setDisable(true);
        RefreshNum.setText("Stopped");
        System.out.println("Stopping");
        runTask.cancel(true);
    }

    @FXML
    public void handleApplyButtonAction(ActionEvent event) {
        try {
            System.out.println("You entered in the Sample Number field: " + sampleNumField.getText());
            System.out.println("You entered in the Refresh Interval field: " + settingsRefreshTimeField.getText());
            settings.setSampNum(Integer.parseInt(sampleNumField.getText()));
            settings.setRefreshTime(Integer.parseInt(settingsRefreshTimeField.getText()));
            settings.setLogLevel((String) settingsLogLeveldrop.getValue());
            tempGrapha.getData().clear();
            humidityGrapha.getData().clear();
            tempGraphb.getData().clear();
            humidityGraphb.getData().clear();
            try {
                SetPrefs(settingsRefreshTimeField.getText(),
                        sampleNumField.getText(),
                        settingsChanIDFieldTabA.getText(),
                        settingsReadAPIField.getText(),
                        settingsTabNameField.getText(),
                        settingsTempFieldA.getValue(),
                        settingsHumidityFieldA.getValue(),
                        settingsChanIDFieldTabB.getText(),
                        settingsReadAPIFieldB.getText(),
                        settingsTabNameFieldB.getText(),
                        settingsTempFieldB.getValue(),
                        settingsHumidityFieldB.getValue(),
                        settingsLogLeveldrop.getValue());
                        setLogLevel((String) settingsLogLeveldrop.getValue());
            } catch (FileNotFoundException ex) {
                log.log(Priority.ERROR, ex);
            }
            try {
                GetPrefs();
            } catch (FileNotFoundException ex) {
                log.log(Priority.ERROR, ex);
            }

        } catch (ThingSpeakException | UnirestException ex) {
                log.log(Priority.ERROR, ex);
        }
    }

    @FXML
    public void handleCancelButtonAction(ActionEvent event) {
        System.out.println("Boo ya");
    }

    //=================Interface elements===============//
    @FXML
    private LineChart<String, Number> tempGrapha;
    @FXML
    private LineChart<String, Number> humidityGrapha;

    @FXML
    private Label RefreshNum;
    @FXML
    private Label RefreshNumLabel;

    @FXML
    private MenuItem autoRefreshMenuItem;

    @FXML
    private MenuItem stopAutoRefreshMenuItem;
    @FXML
    private Tab aTab;

    @FXML
    private Tab bTab;
    @FXML
    private LineChart<String, Number> tempGraphb;
    @FXML
    private LineChart<String, Number> humidityGraphb;

    @FXML
    private Button settingsApplyButton;

    @FXML
    private Button settingsCancelButon;

    @FXML
    private TextField settingsRefreshTimeField;

    @FXML
    private TextField sampleNumField;

    @FXML
    private ChoiceBox<String> settingsTempFieldA;

    @FXML
    private ChoiceBox<String> settingsHumidityFieldA;

    @FXML
    private TextField settingsChanIDFieldTabA;

    @FXML
    private TextField settingsReadAPIField;

    @FXML
    private TextField settingsTabNameField;

    @FXML
    private ChoiceBox<String> settingsTempFieldB;

    @FXML
    private ChoiceBox<String> settingsHumidityFieldB;

    @FXML
    private TextField settingsChanIDFieldTabB;

    @FXML
    private TextField settingsReadAPIFieldB;

    @FXML
    private TextField settingsTabNameFieldB;

    @FXML
    private Gauge Sensor1TempGauge;

    @FXML
    private Gauge Sensor1HumdGauge;

    @FXML
    private Gauge Sensor2TempGauge;

    @FXML
    private Gauge Sensor2HumdGauge;
    
    @FXML
    private Label Sensor1SummaryLabel;
    
    @FXML 
    private Label Sensor2SummaryLabel;
    
        @FXML
    private ChoiceBox<String> settingsLogLeveldrop;

    //=================Initialize and start application=========//
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            BasicConfigurator.configure();

            stopAutoRefreshMenuItem.setDisable(true);
            GetPrefs();
            RefreshNum.setVisible(false);
            RefreshNumLabel.setVisible(false);
            
     log.debug("Hello this is an debug message");
     log.error("Hello this is an error message");

  
        } catch (ThingSpeakException | UnirestException | FileNotFoundException ex) {
                            log.log(Priority.ERROR, ex);
        }

    }

}
