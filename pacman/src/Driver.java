package src;

import src.utility.PropertiesLoader;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "pacman/properties/test1.properties";

    /**
     * Starting point
     * @param args the command line arguments
     */

    public static void main(String args[]) {
        String propertiesPath = DEFAULT_PROPERTIES_PATH;
        if (args.length > 0) {
            propertiesPath = args[0];
        }
        final PropertiesLoader propertiesLoader = new PropertiesLoader(propertiesPath);

        // Run the (Multiverse) PacMan game
        if (propertiesLoader.getProperty("version").equals("simple")){
            SimpleGame simpleGame = new SimpleGame(propertiesLoader);
            simpleGame.run();
        } else {
            MultiverseGame multiverseGame = new MultiverseGame(propertiesLoader);
            multiverseGame.run();
        }

    }
}
