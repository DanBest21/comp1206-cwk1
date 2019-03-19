package comp1206.sushi.server.components;

import comp1206.sushi.server.forms.EntryForm;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel
{
    private Font font = new Font("Viner Hand ITC", Font.BOLD, 20);
    private TableView view;
    private ServerInterface server;

    public ButtonPanel(TableView view, ServerInterface server)
    {
        this.view = view;
        this.server = server;

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
            // TODO: Figure out the best way of distinguishing between an add or edit EntryForm.
            case "Add":
            case "Edit":
                btn.addActionListener(e -> new EntryForm(server, btn.getText()));
        }
    }
}
