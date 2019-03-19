package comp1206.sushi.server.forms;

import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.ServerWindow;

import javax.swing.*;

public class EntryForm extends JFrame
{
    public EntryForm(ServerInterface server, String formName)
    {
        super("Sushi Server");
        this.setTitle(server.getRestaurantName() + " Server - " + formName);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(ServerWindow.getImg());

        setVisible(true);
    }
}
