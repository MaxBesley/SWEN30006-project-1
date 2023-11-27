package src;

import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;

public class WizardMonster extends Monster {

    public WizardMonster(Game game) {
        super(game, "m_wizard.gif");

    }

    @Override
    protected void walkApproach(boolean isFurious) {
        boolean validMove = false;

        // Creates a list of all the available directions the wizard can move in
        ArrayList<Double> availableDirections = new ArrayList<Double>();
        for (double direction = 0; direction < 360; direction += 45) {
            availableDirections.add(direction);
        }

        while (!validMove) {
            // Randomly select a direction
            int randomChoice = randomiser.nextInt(availableDirections.size());
            double chosenRandomDirection = availableDirections.get(randomChoice);

            // The x and y of the selected location given a direction
            Location selectedAdjacentLocation = getLocation().getAdjacentLocation(chosenRandomDirection, 1);

            // If the wizard can move to this adjacent location, this is a valid move and
            // the wizard moves there. We can exit the while loop.
            if (canMove(selectedAdjacentLocation)) {

                if (isFurious){
                    selectedAdjacentLocation = selectedAdjacentLocation.getAdjacentLocation(chosenRandomDirection, 1);
                    if (canMove(selectedAdjacentLocation)){
                        setLocation(selectedAdjacentLocation);
                        addVisitedList(selectedAdjacentLocation);
                        validMove = true;

                    } else if (isMazeWall(selectedAdjacentLocation)){
                        Location adjacentToAdjacentCell = selectedAdjacentLocation.getAdjacentLocation(chosenRandomDirection, 1);

                        if (canMove(adjacentToAdjacentCell)) {
                            setLocation(adjacentToAdjacentCell);
                            addVisitedList(adjacentToAdjacentCell);
                            validMove = true;

                            // If the adjacent location to the adjacent wall is immovable, remove this adjacent wall direction
                            // from the list of directions and retry randomly picking a direction and moving there.
                        } else {
                            availableDirections.remove(randomChoice);
                        }

                    } else {
                        availableDirections.remove(randomChoice);
                    }

                } else {
                    setLocation(selectedAdjacentLocation);
                    addVisitedList(selectedAdjacentLocation);
                    validMove = true;
                }

            // If the adjacent location is a wall
            } else if (isMazeWall(selectedAdjacentLocation)) {
                Location adjacentToAdjacentCell = selectedAdjacentLocation.getAdjacentLocation(chosenRandomDirection, 1);

                // If the adjacent location to the adjacent wall (in the same direction) is a
                // movable spot, then the wizard moves here. We can exit the while loop.
                if (canMove(adjacentToAdjacentCell)) {
                    setLocation(adjacentToAdjacentCell);
                    addVisitedList(adjacentToAdjacentCell);
                    validMove = true;

                // If the adjacent location to the adjacent wall is immovable, remove this adjacent wall direction
                // from the list of directions and retry randomly picking a direction and moving there.
                } else {
                    availableDirections.remove(randomChoice);
                }

            // If the adjacent location to the Wizard is out of bounds, then remove this adjacent location
            // from the list of available directions and retry the process of picking a direction.
            } else {
                availableDirections.remove(randomChoice);
            }
        }

        game.getGameCallback().monsterLocationChanged(this);
    }

    @Override
    public String toString() {
        return "Wizard";
    }
}
