package comp1206.sushi.server.map;

import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.common.User;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.configuration.ServerConfiguration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

// MapView class - Daniel Best, 2019
// The map image comes from the OpenStreetMap, a open source map tool - https://www.openstreetmap.org.
public class MapView extends JPanel
{
    private BufferedImage mapImage;
    private ServerInterface server;

    public MapView(ServerInterface server)
    {
        this.server = server;

        try
        {
            mapImage = ImageIO.read(getClass().getResource("/map.png"));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        repaint();
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D graphics = (Graphics2D) g;

        super.paintComponent(graphics);

        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.drawImage(mapImage, 0, 0, ServerConfiguration.getMapWidth(), ServerConfiguration.getMapHeight(), null);

        drawMakers(graphics);
    }

    private void drawMakers(Graphics2D graphics)
    {
        Postcode postcode = server.getRestaurantPostcode();
        int[] position = calculatePosition(postcode);

        BufferedImage restaurantIcon;

        try
        {
            restaurantIcon = ImageIO.read(getClass().getResource("/icon.png"));
            graphics.drawImage(restaurantIcon, position[0] - 20, position[1] - 20, 40, 40, null);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        for (Supplier supplier : server.getSuppliers())
        {
            graphics.setColor(ServerConfiguration.getColour());

            postcode = supplier.getPostcode();
            position = calculatePosition(postcode);

            graphics.fillRect(position[0] - 5, position[1] - 5, 10, 10);

            graphics.setColor(Color.BLACK);

            graphics.drawRect(position[0] - 5, position[1] - 5, 10, 10);
        }

        for (User user : server.getUsers())
        {
            graphics.setColor(ServerConfiguration.getColour());

            postcode = user.getPostcode();
            position = calculatePosition(postcode);

            graphics.fillOval(position[0] - 5, position[1] - 5, 10, 10);

            graphics.setColor(Color.BLACK);

            graphics.drawOval(position[0] - 5, position[1] - 5, 10, 10);
        }
    }

    private int[] calculatePosition(Postcode postcode)
    {
        int[] position = new int[2];

        double latDifference = ServerConfiguration.getMapLatBoundaries()[0] - ServerConfiguration.getMapLatBoundaries()[1];
        double longDifference = ServerConfiguration.getMapLongBoundaries()[0] - ServerConfiguration.getMapLongBoundaries()[1];

        double latDifference2 = postcode.getLatLong().get("lat") - ServerConfiguration.getMapLatBoundaries()[1];
        double longDifference2 = postcode.getLatLong().get("long") - ServerConfiguration.getMapLongBoundaries()[1];

        double latRatio = latDifference2 / latDifference;
        double longRatio = longDifference2 / longDifference;

        position[0] = (int)Math.round(longRatio * ServerConfiguration.getMapWidth());
        position[1] = (int)Math.round(ServerConfiguration.getMapHeight() - (latRatio * ServerConfiguration.getMapHeight()));

        return position;
    }
}
