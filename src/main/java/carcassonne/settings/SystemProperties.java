package carcassonne.settings;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

/**
 * Encapsules system-specific properties like OS name and screen resolution.
 * @author Timur Saglam
 */
public class SystemProperties {
    private static final String MAC_OS_X = "Mac OS X";
    private static final String WINDOWS_7 = "Windows 7";
    private static final String WINDOWS_10 = "Windows 10";
    private static final String OS_NAME_KEY = "os.name";
    private final String osName;
    private final int resolutionHeight;
    private final int resolutionWidth;
    private final int taskBarHeight;

    /**
     * Creates system properties.
     */
    public SystemProperties() {
        osName = System.getProperty(OS_NAME_KEY);
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        DisplayMode displayMode = environment.getDefaultScreenDevice().getDisplayMode();
        switch (osName) {
        case WINDOWS_7:
            taskBarHeight = 40;
            break;
        case WINDOWS_10:
            taskBarHeight = 60;
            break;
        case MAC_OS_X:
            taskBarHeight = 27;
            break;
        default:
            taskBarHeight = 50;
        }
        resolutionWidth = displayMode.getWidth();
        resolutionHeight = displayMode.getHeight() - taskBarHeight;
    }

    /**
     * Returns the height value of the screen resolution.
     * @return the resolution height in pixel.
     */
    public int getResolutionHeight() {
        return resolutionHeight;
    }

    /**
     * Returns the width value of the screen resolution.
     * @return the resolution width in pixel.
     */
    public int getResolutionWidth() {
        return resolutionWidth;
    }

    /**
     * Returns the estimated task bar height.
     * @return the height in pixel.
     */
    public int getTaskBarHeight() {
        return taskBarHeight;
    }
}
