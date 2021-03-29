package carcassonne.model.ai;

import java.util.Comparator;

/**
 * Comparator to compare moves with an equal value based on simple heuristic rules.
 * @author Timur Saglam
 */
public class RuleBasedComparator implements Comparator<AbstractCarcassonneMove> {

    @Override
    public int compare(AbstractCarcassonneMove firstMove, AbstractCarcassonneMove secondMove) {
        if (firstMove.getGainedMeeples() != secondMove.getGainedMeeples()) {
            // Rule 1: Prefer move with a maximal meeple gain
            return firstMove.getGainedMeeples() - secondMove.getGainedMeeples();
        } else if (firstMove.involvesMeeplePlacement() != secondMove.involvesMeeplePlacement()) {
            // Rule 2: prefer move without meeple placement
            return preferFalse(firstMove.involvesMeeplePlacement(), secondMove.involvesMeeplePlacement());
        }
        // Rule 3: chose in the order of castle > monastery > road > fields
        return rateMoveType(firstMove) - rateMoveType(secondMove);
    }

    private int rateMoveType(AbstractCarcassonneMove move) {
        if (move.involvesMeeplePlacement()) {
            switch (move.getMeepleType()) {
            case CASTLE:
                return 3;
            case MONASTERY:
                return 2;
            case ROAD:
                return 1;
            default:
            }
        }
        return 0; // fields or no meeple placed

    }

    private int preferFalse(Boolean first, Boolean second) {
        return second.compareTo(first);
    }

}
