package comp1206.sushi.server.components;

import comp1206.sushi.server.configuration.ServerConfiguration;
import comp1206.sushi.server.forms.*;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel
{
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
            btn.setFont(ServerConfiguration.getTitleFont());
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
            case "Add":
            case "Edit":
                try
                {
                    btn.addActionListener(getActionListener(btn.getText()));
                }
                catch (IllegalStateException ex)
                {
                    ex.printStackTrace();
                }
        }
    }

    private ActionListener getActionListener(String btnName) throws IllegalStateException
    {
        String operation = btnName.split(" ")[0];
        String className = btnName.split(" ")[1];

        switch (className)
        {
            case "Dish":
                return (e -> new DishForm(server, btnName));
            case "Drone":
                return (e -> new DroneForm(server, btnName));
            case "Ingredient":
                return (e -> new IngredientForm(server, btnName));
            case "Postcode":
                return (e -> new PostcodeForm(server, btnName));
            case "Staff":
                return (e -> new StaffForm(server, btnName));
            case "Supplier":
                return (e -> new SupplierForm(server, btnName));
            default:
                throw new IllegalStateException("Element \"" + btnName + "\" is not recognised.");
        }
    }
}
