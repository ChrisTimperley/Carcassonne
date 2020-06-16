package carcassonne.view.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import carcassonne.control.MainController;
import carcassonne.model.Player;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.terrain.TerrainType;
import carcassonne.view.PaintShop;

/**
 * Special {@link JLabel} for showing meeples.
 * @author Timur Saglam
 */
public class MeepleDepiction {
    private static final int MEEPLE_SCALING_THRESHOLD = 100;
    private static final int INITIAL_MEEPLE_SIZE = 25;
    private Player player;
    private final MouseAdapter mouseAdapter;
    private TerrainType terrain;
    private final JLabel label;
    private boolean preview;
    private int meepleSize;

    /**
     * Creates a blank meeple label.
     * @param scalingFactor is the initial scaling factor of the depiction.
     * @param controller is the {@link MainController} of the game.
     * @param direction is the {@link GridDirection} where the meeple label sits on the tile.
     * @param frame is the main {@link JFrame} to repaint after setting icons.
     */
    public MeepleDepiction(MainController controller, GridDirection direction) {
        meepleSize = INITIAL_MEEPLE_SIZE;
        terrain = TerrainType.OTHER;
        label = new JLabel(PaintShop.getPreviewMeeple(terrain, meepleSize), SwingConstants.CENTER);
        preview = false;
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    controller.requestMeeplePlacement(direction);
                }
            }

            @Override
            public void mouseEntered(MouseEvent event) {
                setMeepleIcon();
            }

            @Override
            public void mouseExited(MouseEvent event) {
                setPreviewIcon();
            }
        };
    }

    /**
     * Refreshes its icon by getting the newest image from the {@link PaintShop}.
     */
    public void refresh() {
        if (terrain != TerrainType.OTHER && !preview) {
            setMeepleIcon();
        }
    }

    /**
     * Resets the label, which means it displays nothing.
     */
    public void reset() {
        terrain = TerrainType.OTHER;
        setEmptyIcon();
        label.removeMouseListener(mouseAdapter);
    }

    /**
     * Sets the icon of the meeple label according to the {@link Player} and terrain type.
     * @param terrain is the terrain type and affects the meeple type.
     * @param player is the {@link Player}, which affects the color.
     */
    public void setIcon(TerrainType terrain, Player player) {
        this.terrain = terrain;
        this.player = player;
        preview = false;
        refresh();
    }

    /**
     * Sets the specific {@link TerrainType} as meeple placement preview, which means a transparent image of the correlating
     * meeple.
     * @param terrain is the specific {@link TerrainType}.
     * @param player is the {@link Player} who is currently active.
     */
    public void setPreview(TerrainType terrain, Player player) {
        this.terrain = terrain;
        this.player = player;
        preview = true;
        label.addMouseListener(mouseAdapter);
        setPreviewIcon();
    }

    /**
     * Grants access to the {@link JLabel} itself.
     * @return the {@link JLabel}
     */
    public JLabel getLabel() {
        return label;
    }

    /**
     * Specifies the size of the meeple and therefore the meeple label.
     * @param scalingFactor the new size in percent. Might be limited by the MEEPLE_SCALING_THRESHOLD. Set to 100 for the
     * original size.
     */
    public void setMeepleSize(int scalingFactor) {
        int limitedFactor = scalingFactor > MEEPLE_SCALING_THRESHOLD ? MEEPLE_SCALING_THRESHOLD : scalingFactor;
        meepleSize = INITIAL_MEEPLE_SIZE * limitedFactor / 100;
        if (terrain == TerrainType.OTHER) {
            setEmptyIcon();
        } else if (preview) {
            setPreviewIcon();
        } else {
            setMeepleIcon();
        }
    }

    private void setEmptyIcon() {
        label.setIcon(PaintShop.getPreviewMeeple(terrain, meepleSize));
    }

    private void setMeepleIcon() {
        label.setIcon(PaintShop.getColoredMeeple(terrain, player, meepleSize));
    }

    private void setPreviewIcon() {
        setEmptyIcon();
    }
}
