/**
 * 
 */
package carcassonne.model.grid;

import carcassonne.model.tile.TerrainType;
import carcassonne.model.tile.Tile;

/**
 * @author Timur Saglam
 */
public class CastleAndRoadPattern extends GridPattern {

    /**
     * Public constructor for creating road and monastery patterns.
     * @param startingTile is the starting tile of the pattern.
     * @param startingDirection is the starting direction of the pattern.
     * @param patternType is the type of the pattern.
     */
    public CastleAndRoadPattern(Tile startingTile, GridDirection startingDirection, TerrainType patternType, Grid grid) {
        super(patternType);
        if (patternType != TerrainType.CASTLE && patternType != TerrainType.ROAD) {
            throw new IllegalArgumentException("Can only create CastleAndRoadPatterns from type castle or road");
        } else if (startingTile == null || startingDirection == null || grid == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }
        buildPattern(startingTile, startingDirection, grid);
    }

    private void buildPattern(Tile startingTile, GridDirection startingPoint, Grid grid) {
        startingTile.setTag(startingPoint);
        add(startingTile);
        if (buildingRecursion(startingTile, startingPoint, grid) && grid.getNeighbour(startingTile, startingPoint) != null) {
            complete = true;
        }
    }

    // TODO (HIGHEST) FIX recursion, wrongly finished patterns. Double patterns. See above.
    private boolean buildingRecursion(Tile startingTile, GridDirection startingPoint, Grid grid) {
        boolean isClosed = true;
        Tile neighbor;
        GridDirection oppositeDirection;
        for (GridDirection direction : GridDirection.directNeighbors()) { // for direction
            if (startingTile.isConnected(startingPoint, direction)) { // if is connected side
                neighbor = grid.getNeighbour(startingTile, direction); // get the neighbor
                if (neighbor != null) { // if the neighbor exists
                    oppositeDirection = GridDirection.opposite(direction);
                    if (!neighbor.isTagged(oppositeDirection)) { // if neighbor not visited yet
                        startingTile.setTag(direction);
                        neighbor.setTag(oppositeDirection); // mark as visited
                        add(neighbor); // add to pattern
                        isClosed = buildingRecursion(neighbor, oppositeDirection, grid);
                    }
                } else {
                    isClosed = false; // open connection, can't be finished pattern.
                }
            }
        }
        return isClosed;
    }

}
