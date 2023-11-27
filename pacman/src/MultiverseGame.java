package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import src.utility.PropertiesLoader;

import java.awt.*;

public class MultiverseGame extends Game{

    private Monster orion = new OrionMonster(this);
    private Monster alien = new AlienMonster(this);
    private Monster wizard = new WizardMonster(this);

    public MultiverseGame(PropertiesLoader propertiesLoader) {
        super(propertiesLoader);

        // Setup Random seeds
        seed = Integer.parseInt(propertiesLoader.getProperty("seed"));
        pacActor.setSeed(seed);
        troll.setSeed(seed);
        tx5.setSeed(seed);
        orion.setSeed(seed);
        alien.setSeed(seed);
        wizard.setSeed(seed);
        addKeyRepeatListener(pacActor);
        setKeyRepeatPeriod(150);
        troll.setSlowDown(3);
        tx5.setSlowDown(3);
        orion.setSlowDown(3);
        alien.setSlowDown(3);
        wizard.setSlowDown(3);
        pacActor.setSlowDown(3);
        tx5.stopMoving(5);

        // setupActorLocations();
        addActor(troll, propertiesLoader.getActorLocation("Troll"), Location.NORTH);
        addActor(pacActor, propertiesLoader.getActorLocation("PacMan"));
        addActor(tx5, propertiesLoader.getActorLocation("TX5"), Location.NORTH);
        addActor(orion, propertiesLoader.getActorLocation("Orion"), Location.NORTH);
        addActor(alien, propertiesLoader.getActorLocation("Alien"), Location.NORTH);
        addActor(wizard, propertiesLoader.getActorLocation("Wizard"), Location.NORTH);

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

            if (pacActor.isFrozen){
                freezeMonsters();
            }

            if (pacActor.isFurious){
                furiousMonsters();
            }

            hasPacmanBeenHit = troll.getLocation().equals(pacActor.getLocation()) ||
                    tx5.getLocation().equals(pacActor.getLocation())||
                    orion.getLocation().equals(pacActor.getLocation())||
                    alien.getLocation().equals(pacActor.getLocation())||
                    wizard.getLocation().equals(pacActor.getLocation());
            hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;

            delay(10);
        } while(!hasPacmanBeenHit && !hasPacmanEatAllPills);
        delay(120);

        Location loc = pacActor.getLocation();
        troll.setStopMoving(true);
        tx5.setStopMoving(true);
        orion.setStopMoving(true);
        alien.setStopMoving(true);
        wizard.setStopMoving(true);
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

    //Make monster furious and move fast
    private void furiousMonsters() {
        troll.fastMoving(3);
        tx5.fastMoving(3);
        orion.fastMoving(3);
        alien.fastMoving(3);
        wizard.fastMoving(3);

        pacActor.isFurious = false;
    }

    //freeze the monsters on some event (like PacMan eats ice cube )
    public void freezeMonsters() {
        troll.stopMoving(3);
        tx5.stopMoving(3);
        orion.stopMoving(3);
        alien.stopMoving(3);
        wizard.stopMoving(3);

        troll.overwriteIsFurious();
        tx5.overwriteIsFurious();
        orion.overwriteIsFurious();
        alien.overwriteIsFurious();
        wizard.overwriteIsFurious();

        pacActor.isFrozen = false;





    }


}
