package carcassonne.control;

import java.util.HashMap;
import java.util.Map;

import carcassonne.control.state.AbstractControllerState;
import carcassonne.control.state.StateGameOver;
import carcassonne.control.state.StateIdle;
import carcassonne.control.state.StateManning;
import carcassonne.control.state.StatePlacing;
import carcassonne.model.Round;
import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.view.main.MainGUI;
import carcassonne.view.main.menubar.Scoreboard;
import carcassonne.view.secondary.PlacementGUI;
import carcassonne.view.secondary.RotationGUI;

/**
 * The MainController is the central class of the game. The game is started with the instantiation
 * of this class. The class gets the user input from the <code>MouseAdapter</code> in the
 * <code>view package</code>, and controls both the <code>view</code> and the <code>model</code>.
 * The <code>controller</code> package also contains the state machine, which consists out of the
 * <code>MainController</code> class and the state classes. This system implements the
 * model/view/controller architecture, which is not 100% formally implemented. The reason for this
 * is that in the user input is made in Swing through the <code>MouseAdapters</code>, which belong
 * to the <code>view</code> package.
 * @author Timur Saglam
 */
public class MainController {
    private final MainGUI mainGUI;
    private final RotationGUI rotationGUI;
    private final PlacementGUI placementGUI;
    private final Scoreboard scoreboard;
    private final Map<Class<? extends AbstractControllerState>, AbstractControllerState> stateMap;
    private AbstractControllerState currentState;

    /**
     * Basic constructor. Creates the view and the model of the game.
     */
    public MainController() {
        scoreboard = new Scoreboard();
        mainGUI = new MainGUI(scoreboard, this);
        rotationGUI = new RotationGUI(this);
        placementGUI = new PlacementGUI(this);
        stateMap = new HashMap<Class<? extends AbstractControllerState>, AbstractControllerState>();
        currentState = new StateIdle(this, mainGUI, rotationGUI, placementGUI, scoreboard);
        new StateManning(this, mainGUI, rotationGUI, placementGUI, scoreboard);
        new StatePlacing(this, mainGUI, rotationGUI, placementGUI, scoreboard);
        new StateGameOver(this, mainGUI, rotationGUI, placementGUI, scoreboard);
    }

    /**
     * Changes the state of the controller to a new state.
     * @param stateType specifies which state is the new state.
     * @return the new state.
     */
    public AbstractControllerState changeState(Class<? extends AbstractControllerState> stateType) {
        // TODO (LOW) Remove debug output for state changes.
        System.out.println("change to " + stateType + " from " + currentState.getClass());                                          // (HIGH)
        currentState = stateMap.get(stateType);
        if (currentState == null) {
            throw new IllegalStateException("State is not registered: " + stateType);
        }
        return currentState;
    }

    /**
     * Registers a specific state at the controller.
     * @param state is the specific state.
     * @param stateType is the class type of the specific state.
     */
    public void registerState(AbstractControllerState state) {
        if (stateMap.put(state.getClass(), state) != null) {
            throw new IllegalArgumentException("Can't register two states of a kind.");
        }
    }

    /**
     * Updates the round and the grid of every state after a new round has been started.
     * @param newRound sets the new round.
     * @param newGrid sets the new grid.
     */
    public void updateStates(Round newRound, Grid newGrid) {
        scoreboard.rebuild(newRound.getPlayerCount());
        for (AbstractControllerState state : stateMap.values()) {
            state.updateState(newRound, newGrid);
        }
    }

    /**
     * Requests to start a new round with a specific amount of players.
     * @param playerCount sets the amount of players.
     * @return true if request was granted.
     */
    public boolean requestNewGame(int playerCount) {
        return currentState.newGame(playerCount);
    }

    /**
     * Requests to abort the round.
     * @param playerCount sets the amount of players.
     * @return true if request was granted.
     */
    public boolean requestAbortGame() {
        return currentState.abortGame();
    }

    /**
     * Method for the view to call if a user places a tile.
     * @param x is the x coordinate.
     * @param y is the y coordinate.
     * @return true if request was granted.
     */
    public boolean requestTilePlacement(int x, int y) {
        return currentState.placeTile(x, y);
    }

    /**
     * Method for the view to call if the user wants to skip a round.
     * @return true if request was granted.
     */
    public boolean requestSkip() {
        return currentState.skip();
    }

    /**
     * Method for the view to call if a user mans a tile with a meeple.
     * @return true if request was granted.
     */
    public boolean requestMeeplePlacement(GridDirection position) {
        return currentState.placeMeeple(position);
    }
}
