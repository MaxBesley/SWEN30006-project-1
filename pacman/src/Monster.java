// Monster.java
// Used for PacMan
package src;

import ch.aplu.jgamegrid.*;
import java.awt.Color;
import java.util.*;

public abstract class Monster extends Actor {
  protected Game game;
  private ArrayList<Location> visitedList = new ArrayList<Location>();
  private final int listLength = 10;
  private boolean stopMoving = false;
  private boolean isFurious = false;
  private int seed = 0;
  protected Random randomiser = new Random(seed);

  public Monster(Game game, String imageName) {
    super("sprites/" + imageName);
    this.game = game;
  }

  protected abstract void walkApproach(boolean isFurious);
  public abstract String toString();

  public void stopMoving(int seconds) {
    this.stopMoving = true;
    Timer timer = new Timer();   // Instantiate Timer Object
    int SECOND_TO_MILLISECONDS = 1000;
    final Monster monster = this;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        monster.stopMoving = false;
      }
    }, seconds * SECOND_TO_MILLISECONDS);
  }

  public void fastMoving(int seconds) {
    this.isFurious = true;
    Timer timer = new Timer();   // Instantiate Timer Object
    int SECOND_TO_MILLISECONDS = 1000;
    final Monster monster = this;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        monster.isFurious = false;
      }
    }, seconds * SECOND_TO_MILLISECONDS);
  }

  public void overwriteIsFurious(){
    this.isFurious = false;
  }

  public void setSeed(int seed) {
    this.seed = seed;
    randomiser.setSeed(seed);
  }

  public void setStopMoving(boolean stopMoving) {
    this.stopMoving = stopMoving;
  }

  public void act() {
    if (stopMoving)
      return;

    if(isFurious){
      walkApproach(true);
    } else{
      walkApproach(false);
    }

    if (getDirection() > 150 && getDirection() < 210)
      setHorzMirror(false);
    else
      setHorzMirror(true);
  }

  protected void addVisitedList(Location location) {
    visitedList.add(location);
    if (visitedList.size() == listLength)
      visitedList.remove(0);
  }

  protected boolean isVisited(Location location) {
    for (Location loc : visitedList)
      if (loc.equals(location))
        return true;
    return false;
  }

  protected boolean canMove(Location location) {
    Color c = getBackground().getColor(location);
    if (c.equals(Color.gray) || location.getX() >= game.getNumHorzCells()
          || location.getX() < 0 || location.getY() >= game.getNumVertCells() || location.getY() < 0)
      return false;
    else
      return true;
  }

  protected boolean isMazeWall(Location location) {
    Color c = getBackground().getColor(location);
    if (c.equals(Color.gray))
      return true;
    else
      return false;
  }

  protected Location moveRandomly(double oldDirection) {
    Location next;

    // Step in a random direction
    int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
    setDirection(oldDirection);
    turn(sign * 90);  // Try to turn left/right
    next = getNextMoveLocation();

    if (!isFurious){
      if (canMove(next))
      {
        setLocation(next);
      }
      else
      {
        setDirection(oldDirection);
        next = getNextMoveLocation();
        if (canMove(next)) // Try to move forward
        {
          setLocation(next);
        }
        else
        {
          setDirection(oldDirection);
          turn(-sign * 90);  // Try to turn right/left
          next = getNextMoveLocation();
          if (canMove(next))
          {
            setLocation(next);
          }
          else
          {
            setDirection(oldDirection);
            turn(180);  // Turn backward
            next = getNextMoveLocation();
            setLocation(next);
          }
        }
      }

      return next;

    } else {

      Location adjacentToAdjacent = next.getNeighbourLocation(getDirection());

      //check if both the adjacent location and the adjacent location^2 is a valid move.
      if (canMove(next) && canMove(adjacentToAdjacent))
      {
        setLocation(adjacentToAdjacent);
      }
      else
      {
        setDirection(oldDirection);
        next = getNextMoveLocation();
        adjacentToAdjacent = next.getNeighbourLocation(getDirection());
        if (canMove(next) && canMove(adjacentToAdjacent)) // Try to move forward
        {
          setLocation(adjacentToAdjacent);
        }
        else
        {
          setDirection(oldDirection);
          turn(-sign * 90);  // Try to turn right/left
          next = getNextMoveLocation();
          adjacentToAdjacent = next.getNeighbourLocation(getDirection());
          if (canMove(next) && canMove(adjacentToAdjacent))
          {
            setLocation(adjacentToAdjacent);
          }
          else
          {
            setDirection(oldDirection);
            turn(180);  // Turn backward
            next = getNextMoveLocation();
            adjacentToAdjacent = next.getNeighbourLocation(getDirection());
            setLocation(adjacentToAdjacent);
          }
        }
      }

      return adjacentToAdjacent;
    }


  }
}
