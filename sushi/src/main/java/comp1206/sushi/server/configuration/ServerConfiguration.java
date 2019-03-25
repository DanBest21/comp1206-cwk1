package comp1206.sushi.server.configuration;

import javax.swing.*;
import java.awt.*;

// ServerConfiguration class - Daniel Best, 2019
public final class ServerConfiguration
{
    private static final Color RED = new Color(170, 50, 50);
    private static final Font TITLE_FONT = new Font("Viner Hand ITC", Font.BOLD, 20);
    private static final Font FONT = new Font("Courier New", Font.PLAIN, 16);
    private static final ImageIcon IMG = new ImageIcon("src/main/resources/icon.png");
    private static final int MAP_WIDTH = (1195 * 75) / 100;
    private static final int MAP_HEIGHT = (1218 * 75) / 100;
    private static final double[] MAP_LAT_BOUNDARIES = new double[]{50.9657, 50.8814};
    private static final double[] MAP_LONG_BOUNDARIES = new double[]{-1.3499, -1.4808};

    private static final String[] TABS = {"Orders", "Dishes", "Ingredients", "Suppliers", "Staff", "Users", "Drones", "Postcodes", "Map"/*, "Configuration"*/};

    public static Color getColour() { return RED; }

    public static Font getTitleFont() { return TITLE_FONT; }

    public static Font getSmallTitleFont() { return TITLE_FONT.deriveFont((float)16).deriveFont(Font.PLAIN); }

    public static Font getFont() { return FONT; }

    public static Image getImg()
    {
        return IMG.getImage();
    }

    public static String[] getTabs() { return TABS; }

    public static int getMapWidth() { return MAP_WIDTH; }

    public static int getMapHeight() { return MAP_HEIGHT; }

    public static double[] getMapLatBoundaries() { return MAP_LAT_BOUNDARIES; }

    public static double[] getMapLongBoundaries() { return MAP_LONG_BOUNDARIES; }
}
