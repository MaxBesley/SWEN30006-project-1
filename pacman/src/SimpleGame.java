package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import src.utility.PropertiesLoader;

import java.awt.*;

public class SimpleGame extends Game{

    public SimpleGame(PropertiesLoader propertiesLoader) {
        super(propertiesLoader);

        // Setup Random seeds
        seed = Integer.parseInt(propertiesLoader.getProperty("seed"));
        pacActor.setSeed(seed);
        troll.setSeed(seed);
        tx5.setSeed(seed);
        addKeyRepeatListener(pacActor);
        setKeyRepeatPeriod(150);
        troll.setSlowDown(3);
        tx5.setSlowDown(3);

        pacActor.setSlowDown(3);
        tx5.stopMoving(5);

        // setupActorLocations();
        addActor(troll, propertiesLoader.getActorLocation("Troll"), Location.NORTH);
        addActor(pacActor, propertiesLoader.getActorLocation("PacMan"));
        addActor(tx5, propertiesLoader.getActorLocation("TX5"), Location.NORTH);

    }

    public void run() {
        // Run the game
        doRun();
        show();
        // Loop to look for collision in the application thread
        // This makes it improbable that we miss a hit
        boolean hasPacmanBeenHit;
        boolean hasPacmanEatAllPills;
        setupPillAndItemsLocations();
        int maxPillsAndItems = countPillsAndItems();

        //This can be simplified. We should be able to find all the monster Actors locations in one hit.
        do {
            hasPacmanBeenHit = troll.getLocation().equals(pacActor.getLocation()) ||
                    tx5.getLocation().equals(pacActor.getLocation());

            hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;

            delay(10);
        } while(!hasPacmanBeenHit && !hasPacmanEatAllPills);
        delay(120);

        Location loc = pacActor.getLocation();
        troll.setStopMoving(true);
        tx5.setStopMoving(true);
        pacActor.removeSelf();

        String title = "";
        if (hasPacmanBeenHit) {
            bg.setPaintColor(Color.red);
            title = "GAME OVER";
            addActor(new Actor("sprites/explosion3.gif"), loc);
        } else if (hasPacmanEatAllPills) {
            bg.setPaintColor(Color.yellow);
            title = "YOU WIN";
        }
        setTitle(title);
        gameCallback.endOfGame(title);

        doPause();
    }
}
