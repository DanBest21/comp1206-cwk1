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
            case "Configuration":
                buttons = new JButton[0];
                break;

            default:
                throw new IllegalStateException("Tab \"" + tab + "\" is not recognised.");
        }

        for (JButton btn : buttons)
        {
            btn.setFont(ServerConfiguration.getTitleFont());
            btn.setBackground(Color.WHITE);
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
                break;
            case "Add":
                try
                {
                    btn.addActionListener(getAddActionListener(btn.getText()));
                }
                catch (IllegalStateException ex)
                {
                    ex.printStackTrace();
                }
                break;
            case "Edit":
                try
                {
                    btn.addActionListener(getEditActionListener(btn.getText()));
                }
                catch (IllegalStateException ex)
                {
                    ex.printStackTrace();
                }
        }
    }

    private ActionListener getAddActionListener(String btnName) throws IllegalStateException
    {
        String className = btnName.split(" ")[1];

        switch (className)
        {
            case "Dish":
                return (e -> new DishForm(server, view, btnName));
            case "Drone":
                return (e -> new DroneForm(server, view, btnName));
            case "Ingredient":
                return (e -> new IngredientForm(server, view, btnName));
            case "Postcode":
                return (e -> new PostcodeForm(server, view, btnName));
            case "Staff":
                return (e -> new StaffForm(server, view, btnName));
            case "Supplier":
                return (e -> new SupplierForm(server, view, btnName));
            default:
                throw new IllegalStateException("Element \"" + btnName + "\" is not recognised.");
        }
    }

    private ActionListener getEditActionListener(String btnName) throws IllegalStateException
    {
        String className = btnName.split(" ")[1];

        switch (className)
        {
            case "Dish":
                return (e -> {
                    if (view.getSelectedTable().getSelectedRow() != -1)
                    {
                        new DishForm(server, view, btnName, view.getParser().getModel("Dishes", view.getSelectedTable()));
                    }
                });
            case "Ingredient":
                return (e -> {
                    if (view.getSelectedTable().getSelectedRow() != -1)
                    {
                        new IngredientForm(server, view, btnName, view.getParser().getModel("Ingredients", view.getSelectedTable()));
                    }
                });
            default:
                throw new IllegalStateException("Element \"" + btnName + "\" is not recognised.");
        }
    }
}
