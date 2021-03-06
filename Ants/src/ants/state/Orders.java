package ants.state;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logging.Logger;
import logging.LoggerFactory;
import ants.LogCategory;
import ants.entities.Ant;
import ants.util.LiveInfo;
import api.entities.Aim;
import api.entities.Move;
import api.entities.Tile;
import api.search.PathPiece;

/**
 * This class tracks all orders and missions for our ants. It ensures that no conflicting orders are given.
 * 
 * @author kases1, kustl1
 * 
 */
public class Orders {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogCategory.ORDERS);
    private Map<Tile, Move> orders = new HashMap<Tile, Move>();

    /**
     * Clears all turn-scoped state (i.e. the orders); the missions are tracked across turns.
     */
    public void clearState() {
        orders.clear();
        // prevent stepping on own hill
        for (Tile myHill : Ants.getWorld().getMyHills()) {
            orders.put(myHill, new Move(myHill, null));
        }
    }

    /**
     * Move the ant to the next tile in the given direction. This might fail if the tile is occupied, not passable, or
     * if another ant was already sent there.
     * 
     * If the order is placed successfully, the nextTile attribute of the ant is updated and the ant is marked as
     * employed.
     * 
     * @param ant
     * @param direction
     * @param issuer
     *            who gave the order? for logging purposes only
     * @return true if the order was successfully placed
     */
    public boolean issueOrder(Ant ant, Aim direction, String issuer) {
        Tile newLoc = ant.getTile();
        if (direction != null)
            newLoc = Ants.getWorld().getTile(ant.getTile(), direction);
        if (direction == null || isMovePossible(newLoc)) {
            LiveInfo.liveInfo(Ants.getAnts().getTurn(), ant.getTile(), "Task: %s Order:%s<br/> ant: %s", issuer,
                    direction, ant.visualizeInfo());
            LOGGER.debug("%1$s: Moving ant from %2$s to %3$s", issuer, ant.getTile(), newLoc);
            // Track all moves, prevent collisions
            orders.put(newLoc, new Move(ant.getTile(), direction));
            ant.setNextTile(newLoc);
            Ants.getPopulation().addEmployedAnt(ant);
            return true;
        } else {
            LOGGER.debug("Move is not possible %s to %s", ant, newLoc);
            return false;
        }
    }

    /**
     * @param newLoc
     * @return if we can move to newLoc
     */
    public boolean isMovePossible(Tile newLoc) {
        if (isSurroundedByWater(newLoc))
            return false; // it's a trap!
        if (!isFreeForNextMove(newLoc))
            return false;
        return true;
    }

    private boolean isSurroundedByWater(Tile tile) {
        int unpassableNeighbourTiles = 0;
        for (Aim aim : Aim.values()) {
            if (!Ants.getWorld().isPassable(Ants.getWorld().getTile(tile, aim)))
                unpassableNeighbourTiles++;
        }
        return unpassableNeighbourTiles >= 3;
    }

    private boolean isFreeForNextMove(Tile nextLocation) {
        String sLog = "isFreeForNextMove: ";
        // there is already an order heading to this field
        boolean hasOrder = orders.containsKey(nextLocation);
        if (hasOrder)
            return false;

        if (Ants.getWorld().getIlk(nextLocation).isUnoccupied())
            return true;
        // the field is occupied at the moment, but the ant is moving away with the next move.
        sLog += "check if ant will go away, neighbours are: ";
        List<PathPiece> neighbours = Ants.getWorld().getSuccessorsForPathfinding(nextLocation, false);
        sLog += neighbours;
        for (PathPiece neighbour : neighbours) {
            if (orders.containsKey(neighbour.getTargetTile())) {
                sLog += "there is a order to neighbour " + neighbour;
                // there is a move where a ant goes to a neighbour cell, maybe it's the ant of "nextlocation"
                Move m = orders.get(neighbour);
                sLog += "move comes from " + m.getTile();
                if (m.getTile().equals(nextLocation)) {
                    // yes it is the ant

                    return true;
                }
            }
        }
        LOGGER.debug(sLog);
        return false;
    }

    /**
     * Prints the orders to the SystemOutputStream (sends them to the game engine).
     */
    public void issueOrders() {
        for (Move move : orders.values()) {
            if (move != null && move.getDirection() != null) {
                final String order = "o " + move.getTile().getRow() + " " + move.getTile().getCol() + " "
                        + move.getDirection().getSymbol();
                LOGGER.debug("Issuing order: %s", order);
                System.out.println(order);
            } else {
                LOGGER.debug("NOT Issuing order: %s", move.getTile());
            }
        }
    }

    /*
     * Accessors
     */

    public Map<Tile, Move> getOrders() {
        return orders;
    }

}
