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
        String name = (tab == "Dishes") ? "Dish" : (tab.charAt(tab.length() - 1) == 's') ? tab.substring(0, tab.length() - 1) : tab;

        switch (tab)
        {
            case "Postcodes":
            case "Drones":
            case "Staff":
            case "Suppliers":
                buttons = new JButton[2];
                buttons[0] = new JButton("Add " + name);
                buttons[1] = new JButton("Remove " + name);
                break;

            case "Dishes":
            case "Ingredients":
                buttons = new JButton[3];
                buttons[0] = new JButton("Add " + name);
                buttons[1] = new JButton("Edit " + name);
                buttons[2] = new JButton("Remove " + name);
                break;

            case "Orders":
            case "Users":
            case "Map":
                buttons = new JButton[0];
                break;

            default:
                throw new IllegalStateException("Tab \"" + tab + "\" is not recognised.");
        }

        for (JButton btn : buttons)
        {
            btn.setFont(font);
            createButtonListener(btn);
            add(btn);
        }
    }

    private void createButtonListener(JButton btn)
    {
        switch (btn.getText().split(" ")[0])
        {
            case "Remove":
                btn.addActionListener(e -> view.removeRow());
        }
    }
}
