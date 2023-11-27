package src;

import ch.aplu.jgamegrid.Location;

public class TrollMonster extends Monster {

    public TrollMonster(Game game) {
        super(game, "m_troll.gif");
    }

    protected void walkApproach(boolean isFurious) {
        Location pacLocation = game.pacActor.getLocation();
        Location next;

        // Walking approach:
        // TX5: Determine direction to pacActor and try to move in that direction. Otherwise, random walk.
        // Troll: Random walk.
        Location.CompassDirection compassDir =
                getLocation().get4CompassDirectionTo(pacLocation);
        setDirection(compassDir);
        next = moveRandomly(this.getDirection());

        game.getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }

    @Override
    public String toString() {
        return "Troll";
    }

    public String getImageName() {
        return "m_troll.gif";
    }
}
