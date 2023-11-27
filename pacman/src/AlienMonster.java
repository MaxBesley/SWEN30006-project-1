package src;

import ch.aplu.jgamegrid.Location;
import java.util.Random;

public class AlienMonster extends Monster {

    public AlienMonster(Game game) {
        super(game, "m_alien.gif");
    }

    @Override
    protected void walkApproach(boolean isFurious) {
        Location pacLocation = game.pacActor.getLocation();
        Location bestAdjacentLocation = null;

        // The initial distance to compare the first adjacent cell to
        int bestDistanceToPac = Integer.MAX_VALUE;

        // Iterate through all the eight neighbour cells
        for (double direction = 0; direction < 360; direction += 45) {
            Location tempAdjacentLocation = getLocation().getAdjacentLocation(direction, 1);

            // Check if this adjacent cell is a viable cell (i.e. not a maze wall) otherwise skip it
            if (canMove(tempAdjacentLocation)) {

                if (isFurious){
                    //adjacent to adjacent location
                    tempAdjacentLocation = tempAdjacentLocation.getAdjacentLocation(direction, 1);
                    if (canMove(tempAdjacentLocation)){
                        // distance to Pacman from the adjacent^2 location.
                        int tempDistanceToPac = tempAdjacentLocation.getDistanceTo(pacLocation);

                        // Check if this distance is better/smaller
                        if (tempDistanceToPac < bestDistanceToPac) {
                            bestDistanceToPac = tempDistanceToPac;
                            bestAdjacentLocation = tempAdjacentLocation;

                            // If both distances are the same then randomly choose one cell
                        } else if (tempDistanceToPac == bestDistanceToPac) {
                            // Make a random choice
                            if (randomiser.nextBoolean()) {
                                bestDistanceToPac = tempDistanceToPac;
                                bestAdjacentLocation = tempAdjacentLocation;
                            }
                        }
                    }

                } else {
                    // Calculate distance to PacMan
                    int tempDistanceToPac = tempAdjacentLocation.getDistanceTo(pacLocation);

                    // Check if this distance is better/smaller
                    if (tempDistanceToPac < bestDistanceToPac) {
                        bestDistanceToPac = tempDistanceToPac;
                        bestAdjacentLocation = tempAdjacentLocation;

                        // If both distances are the same then randomly choose one cell
                    } else if (tempDistanceToPac == bestDistanceToPac) {
                        // Make a random choice
                        if (randomiser.nextBoolean()) {
                            bestDistanceToPac = tempDistanceToPac;
                            bestAdjacentLocation = tempAdjacentLocation;
                        }
                    }
                }

            }
        }

        //This is the edge case where the alien is in furious mode and cannot move any direction for two spaces.
        // The alien will implement its walk approach for one step then.
        if (bestAdjacentLocation == null){
            walkApproach(false);
            return;
        }

        // Set the new Alien location
        setLocation(bestAdjacentLocation);

        game.getGameCallback().monsterLocationChanged(this);
        addVisitedList(bestAdjacentLocation);
    }

    @Override
    public String toString() {
        return "Alien";
    }
}
