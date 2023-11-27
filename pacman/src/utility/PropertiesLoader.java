package src.utility;

import ch.aplu.jgamegrid.Location;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesLoader {
    private Properties properties;

    public PropertiesLoader(String propertiesFile){
        properties = new Properties();
        loadPropertiesFile(propertiesFile);
    }
    public void loadPropertiesFile(String propertiesFile) {
        try (InputStream input = new FileInputStream(propertiesFile)) {


            // load a properties file
            properties.load(input);
            if( properties.getProperty("PacMan.move").equals("")){
                properties.remove("PacMan.move");
            }

            if( properties.getProperty("Pills.location").equals("")){
                properties.remove("Pills.location");
            }

            if( properties.getProperty("Gold.location").equals("")){
                properties.remove("Gold.location");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public Location getActorLocation(String actor){
        String[] actorLocations = this.properties.getProperty(actor + ".location").split(",");
        int actorX = Integer.parseInt(actorLocations[0]);
        int actorY = Integer.parseInt(actorLocations[1]);

        return new Location(actorX,actorY);
    }

    public ArrayList<Location> getPropertyPillLocations(){
        ArrayList<Location> propertyPillLocations = new ArrayList<>();

        String pillsLocationString = properties.getProperty("Pills.location");
        if (pillsLocationString != null) {
            String[] singlePillLocationStrings = pillsLocationString.split(";");
            for (String singlePillLocationString: singlePillLocationStrings) {
                String[] locationStrings = singlePillLocationString.split(",");
                propertyPillLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
            }
        }

        return propertyPillLocations;
    }
    public ArrayList<Location> getPropertyGoldLocations() {
        ArrayList<Location> propertyGoldLocations = new ArrayList<>();

        String goldLocationString = properties.getProperty("Gold.location");
        if (goldLocationString != null) {
            String[] singleGoldLocationStrings = goldLocationString.split(";");
            for (String singleGoldLocationString: singleGoldLocationStrings) {
                String[] locationStrings = singleGoldLocationString.split(",");
                propertyGoldLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
            }
        }

        return propertyGoldLocations;
    }

    public String getProperty(String property){
        return properties.getProperty(property);
    }

}
