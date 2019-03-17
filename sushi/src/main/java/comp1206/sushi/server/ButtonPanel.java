package comp1206.sushi.server;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel
{
    private Font font = new Font("Viner Hand ITC", Font.BOLD, 20);
    private TableView view;

    public ButtonPanel(TableView view)
    {
        this.view = view;

        setLayout(new GridLayout());
        setBackground(Color.WHITE);

        try
        {
            generateButtons();
        }
        catch (IllegalStateException ex)
        {
            System.err.println(ex);
        }
    }

    public void generateButtons() throws IllegalStateException
    {
        this.removeAll();

        JButton[] buttons;

        String tab = view.getTitleAt(view.getSelectedIndex());

        switch (tab)
        {
            case "Postcodes":
            case "Drones":
            case "Staff":
                buttons = new JButton[2];
                buttons[0] = new JButton("Add " + ((tab.charAt(tab.length() - 1) == 's') ? tab.substring(0, tab.length() - 1) : tab));
                buttons[1] = new JButton("Remove " + ((tab.charAt(tab.length() - 1) == 's') ? tab.substring(0, tab.length() - 1) : tab));
                break;

            case "Suppliers":
            case "Ingredients":
                buttons = new JButton[1];
                buttons[0] = new JButton("Add " + tab.substring(0, tab.length() - 1));
                break;

            case "Dishes":
                buttons = new JButton[2];
                buttons[0] = new JButton("Add Dish");
                buttons[1] = new JButton("Edit Dish");
                break;

            case "Orders":
            case "Users":
            case "Configuration":
                buttons = new JButton[0];
                break;

            default:
                throw new IllegalStateException("Tab \"" + tab + "\" is not recognised.");
        }

        for (JButton btn : buttons)
        {
            btn.setFont(font);
            add(btn);
        }
    }
}
