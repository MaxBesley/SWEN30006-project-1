package src;

import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;
import java.util.Iterator;

public class OrionMonster extends Monster {
    private ArrayList<Location> visitedGold = new ArrayList<Location>();
    private Location nextGoldLoc = null;
    private int numGold = 0;
    private boolean goToNext = true;

    public OrionMonster(Game game) {
        super(game, "m_orion.gif");
    }

    @Override
    protected void walkApproach(boolean isFurious) {
        if (numGold == 0)
            numGold = game.countOfGold();

        if (goToNext) {
            ArrayList<Location>visibleGold = game.getGoldLocation(true);
            ArrayList<Location>eatenGold = game.getGoldLocation(false);

            //check all the visible gold location and prioritise them if not visited
            Location tmpGold;
            Iterator<Location> iter = visibleGold.iterator();
            while (iter.hasNext()) {
                tmpGold = iter.next();
                if (isGoldVisited(tmpGold))
                    iter.remove();
            }
            iter = eatenGold.iterator();
            while (iter.hasNext()) {
                tmpGold = iter.next();
                if (isGoldVisited(tmpGold))
                    iter.remove();
            }
            //pick visible gold to move towards if available
            int index;
            if (visibleGold.size() > 0) {
                index = randomiser.nextInt(visibleGold.size());
                nextGoldLoc = visibleGold.get(index);
            } else {
                index = randomiser.nextInt(eatenGold.size());
                nextGoldLoc = eatenGold.get(index);
            }
            goToNext = false;
        }

        double tempDir = this.getLocation().getDirectionTo(nextGoldLoc);
        Location next = this.getLocation().getNeighbourLocation(tempDir);



        if (!isVisited(next) && canMove(next)){

            if (isFurious){
                next = next.getNeighbourLocation(tempDir);
                if (!isVisited(next) && canMove(next)){
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

        if (this.getLocation().equals(nextGoldLoc)) {
            visitedGold.add(nextGoldLoc);
            goToNext = true;
        }
        if (visitedGold.size() >= numGold)
            visitedGold.clear();
    }

    private boolean isGoldVisited(Location location) {
        for (Location loc : visitedGold) {
            if (loc.equals(location))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Orion";
    }

    public String getImageName() {
        return "m_orion.gif";
    }
}

