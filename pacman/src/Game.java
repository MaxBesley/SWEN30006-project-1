// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;
import src.utility.GameCallback;
import src.utility.PropertiesLoader;

import java.awt.*;
import java.util.ArrayList;

public abstract class Game extends GameGrid {
  private final static int nbHorzCells = 20;
  private final static int nbVertCells = 11;
  protected PacManGameGrid grid = new PacManGameGrid(nbHorzCells, nbVertCells);

  protected PacActor pacActor = new PacActor(this);
  protected Monster troll = new TrollMonster(this);
  protected Monster tx5 = new TX5Monster(this);

  private ArrayList<Location> pillAndItemLocations = new ArrayList<Location>();
  private ArrayList<Actor> iceCubes = new ArrayList<Actor>();
  private ArrayList<Actor> goldPieces = new ArrayList<Actor>();
  protected GameCallback gameCallback;
  private PropertiesLoader propertiesLoader;
  protected int seed = 30006;
  private ArrayList<Location> propertyPillLocations;
  private ArrayList<Location> propertyGoldLocations;
  protected GGBackground bg;

  public Game(PropertiesLoader propertiesLoader) {
    // Setup game
    super(nbHorzCells, nbVertCells, 20, false);
    this.gameCallback = gameCallback;
    this.propertiesLoader = propertiesLoader;
    setSimulationPeriod(100);
    setTitle("[PacMan in the Multiverse]");

    // initialise gamecallback
    this.gameCallback = new GameCallback();

    // Setup for auto test
    pacActor.setPropertyMoves(propertiesLoader.getProperty("PacMan.move"));
    pacActor.setAuto(Boolean.parseBoolean(propertiesLoader.getProperty("PacMan.isAuto")));

    // loadPillAndItemsLocations();
    propertyPillLocations = propertiesLoader.getPropertyPillLocations();
    propertyGoldLocations = propertiesLoader.getPropertyGoldLocations();

    // Get and draw the background
    this.bg = getBg();
    drawGrid(bg);
  }

  public abstract void run();
  public GameCallback getGameCallback() {
    return gameCallback;
  }

  protected int countPillsAndItems() {
    int pillsAndItemsCount = 0;
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 1 && propertyPillLocations.size() == 0) { // Pill
          pillsAndItemsCount++;
        } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
          pillsAndItemsCount++;
        }
      }
    }
    if (propertyPillLocations.size() != 0) {
      pillsAndItemsCount += propertyPillLocations.size();
    }

    if (propertyGoldLocations.size() != 0) {
      pillsAndItemsCount += propertyGoldLocations.size();
    }

    return pillsAndItemsCount;
  }

  public ArrayList<Location> getPillAndItemLocations() {
    return pillAndItemLocations;
  }

  protected void setupPillAndItemsLocations() {
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 1 && propertyPillLocations.size() == 0) {
          pillAndItemLocations.add(location);
        }
        if (a == 3 &&  propertyGoldLocations.size() == 0) {
          pillAndItemLocations.add(location);
        }
        if (a == 4) {
          pillAndItemLocations.add(location);
        }
      }
    }

    if (propertyPillLocations.size() > 0) {
      for (Location location : propertyPillLocations) {
        pillAndItemLocations.add(location);
      }
    }
    if (propertyGoldLocations.size() > 0) {
      for (Location location : propertyGoldLocations) {
        pillAndItemLocations.add(location);
      }
    }
  }

  private void drawGrid(GGBackground bg) {
    bg.clear(Color.gray);
    bg.setPaintColor(Color.white);
    for (int y = 0; y < nbVertCells; y++) {
      for (int x = 0; x < nbHorzCells; x++) {
        bg.setPaintColor(Color.white);
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a > 0)
          bg.fillCell(location, Color.lightGray);
        if (a == 1 && propertyPillLocations.size() == 0) { // Pill
          putPill(bg, location);
        } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
          putGold(bg, location);
        } else if (a == 4) {
          putIce(bg, location);
        }
      }
    }

    for (Location location : propertyPillLocations) {
      putPill(bg, location);
    }

    for (Location location : propertyGoldLocations) {
      putGold(bg, location);
    }
  }

  private void putPill(GGBackground bg, Location location) {
    bg.fillCircle(toPoint(location), 5);
  }

  private void putGold(GGBackground bg, Location location) {
    bg.setPaintColor(Color.yellow);
    bg.fillCircle(toPoint(location), 5);
    Actor gold = new Actor("sprites/gold.png");
    this.goldPieces.add(gold);
    addActor(gold, location);
  }

  private void putIce(GGBackground bg, Location location) {
    bg.setPaintColor(Color.blue);
    bg.fillCircle(toPoint(location), 5);
    Actor ice = new Actor("sprites/ice.png");
    this.iceCubes.add(ice);
    addActor(ice, location);
  }

  public void removeItem(String type,Location location) {
    if (type.equals("gold")) {
      for (Actor item : this.goldPieces) {
        if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
          item.hide();
        }
      }
    } else if(type.equals("ice")) {
      for (Actor item : this.iceCubes) {
        if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
          item.hide();
        }
      }
    }
  }

  public int getNumHorzCells() {
    return this.nbHorzCells;
  }
  public int getNumVertCells() {
    return this.nbVertCells;
  }

  //Returns an array list of all the visible goldlocation on the grid
  public  ArrayList<Location> getGoldLocation(boolean visible) {
    ArrayList<Location> goldLocs = new ArrayList<Location>();
    for (Actor gold : this.goldPieces) {
      if (gold.isVisible() == visible)
        goldLocs.add(gold.getLocation());
    }
    return goldLocs;
  }

  public int countOfGold() {
    return goldPieces.size();
  }


}



