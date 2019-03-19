package comp1206.sushi.server.configuration;

import javax.swing.*;
import java.awt.*;

public final class ServerConfiguration
{
    private static final Color RED = new Color(170, 50, 50);
    private static final Font TITLE_FONT = new Font("Viner Hand ITC", Font.BOLD, 20);
    private static final Font FONT = new Font("Courier New", Font.PLAIN, 16);
    private static final ImageIcon IMG = new ImageIcon("icon.png");

    private static final String[] TABS = {"Orders", "Dishes", "Ingredients", "Suppliers", "Staff", "Users", "Drones", "Postcodes", "Map"};

    public static Color getColour() { return RED; }

    public static Font getTitleFont() { return TITLE_FONT; }

    public static Font getSmallTitleFont() { return TITLE_FONT.deriveFont((float)16).deriveFont(Font.PLAIN); }

    public static Font getFont() { return FONT; }

    public static Image getImg()
    {
        return IMG.getImage();
    }

    public static String[] getTabs() { return TABS; }
}
