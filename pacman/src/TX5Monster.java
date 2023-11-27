package src;

import ch.aplu.jgamegrid.Location;

public class TX5Monster extends Monster {
    public TX5Monster(Game game) {
        super(game, "m_tx5.gif");
    }

    protected void walkApproach(boolean isFurious) {
        Location pacLocation = game.pacActor.getLocation();

        // Walking approach:
        // TX5: Determine direction to pacActor and try to move in that direction. Otherwise, random walk.
        // Troll: Random walk.

        Location.CompassDirection compassDir =
                getLocation().get4CompassDirectionTo(pacLocation);
        Location next = getLocation().getNeighbourLocation(compassDir);
        setDirection(compassDir);

        if (!isVisited(next) && canMove(next)) {
            if (isFurious) {
                //adjacent to adjacent location
                next = next.getNeighbourLocation(compassDir);

                if (!isVisited(next) && canMove(next)) {
                    setLocation(next);
                } else {
                    next = moveRandomly(this.getDirection());
                }

            } else {
                setLocation(next);
            }

        } else {
            next = moveRandomly(this.getDirection());
        }

        game.getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }

    @Override
    public String toString() {
        return "TX5";
    }

    public String getImageName() {
        return "m_tx5.gif";
    }
}
