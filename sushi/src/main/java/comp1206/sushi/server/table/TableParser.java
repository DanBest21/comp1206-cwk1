package comp1206.sushi.server.table;

import comp1206.sushi.common.*;
import comp1206.sushi.server.ServerInterface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TableParser class - Daniel Best, 2019
public class TableParser
{
    private static HashMap<String, List<? extends Model>> loadedData = new HashMap<>();
    private static ServerInterface server;

    public TableParser(ServerInterface server)
    {
        this.server = server;
    }

    public void loadData(String type, DefaultTableModel model)
    {
        List<? extends Model> data = getData(type);

        if (data.size() <= 0)
            return;

        TableData tableData = new TableData(server);
        tableData.loadData(data, model);

        loadedData.put(type, data);
    }

    public List<? extends Model> getData(String type)
    {
        List<? extends Model> data = new ArrayList<>();

        switch (type)
        {
            case "Orders":
                data = server.getOrders();
                break;

            case "Dishes":
                data = server.getDishes();
                break;

            case "Ingredients":
                data = server.getIngredients();
                break;

            case "Suppliers":
                data = server.getSuppliers();
                break;

            case "Staff":
                data = server.getStaff();
                break;

            case "Users":
                data = server.getUsers();
                break;

            case "Drones":
                data = server.getDrones();
                break;

            case "Postcodes":
                data = server.getPostcodes();
                break;

            case "Map":
                break;

            case "Configuration":
                break;

            default:
                displayErrorMessage("The tab \"" + type + "\" is not recognised.", false);
        }

        return data;
    }

    public boolean removeRow(String tab, JTable table)
    {
        switch (tab)
        {
            case "Postcodes":
                try
                {
                    Postcode postcode = getModel(tab, table);

                    for (Supplier supplier : server.getSuppliers())
                    {
                        if (supplier.getPostcode().equals(postcode))
                            throw new ServerInterface.UnableToDeleteException("Unable to delete postcode \"" + postcode.getName() + "\" as the supplier \"" + supplier.getName() + "\" is dependent upon it.");
                    }

                    server.removePostcode(getModel(tab, table));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    displayErrorMessage(ex.getMessage(), true);
                    return false;
                }
                break;

            case "Drones":
                try
                {
                    server.removeDrone(getModel(tab, table));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    displayErrorMessage(ex.getMessage(), true);
                    return false;
                }
                break;

            case "Staff":
                try
                {
                    server.removeStaff(getModel(tab, table));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    displayErrorMessage(ex.getMessage(), true);
                    return false;
                }
                break;

            case "Suppliers":
                try
                {
                    server.removeSupplier(getModel(tab, table));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    displayErrorMessage(ex.getMessage(), true);
                    return false;
                }
                break;

            case "Ingredients":
                try
                {
                    Ingredient ingredient = getModel(tab, table);

                    for (Dish dish : server.getDishes())
                    {
                        if (dish.getRecipe().containsKey(ingredient))
                            throw new ServerInterface.UnableToDeleteException("Unable to delete ingredient \"" + ingredient.getName() + "\", as the dish \"" + dish.getName() + "\" is dependent upon it.");
                    }

                    server.removeIngredient(getModel(tab, table));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    displayErrorMessage(ex.getMessage(), true);
                    return false;
                }
                break;

            case "Dishes":
                try
                {
                    server.removeDish(getModel(tab, table));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    displayErrorMessage(ex.getMessage(), true);
                    return false;
                }
                break;

            case "Users":
                try
                {
                    server.removeUser(getModel(tab, table));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    displayErrorMessage(ex.getMessage(), true);
                    return false;
                }
                break;

            case "Orders":
                try
                {
                    Order order = getModel(tab, table);

                    if (!order.getStatus().toLowerCase().contains("complete"))
                        throw new ServerInterface.UnableToDeleteException("The selected order is still in progress.\r\nAn order may only be deleted once it is complete.");

                    server.removeOrder(order);
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    displayErrorMessage(ex.getMessage(), true);
                    return false;
                }
                break;
        }

        return true;
    }

    public <T extends Model> T getModel(String tab, JTable table)
    {
        List<? extends Model> objects = loadedData.get(tab);
        return (T)objects.get(table.getSelectedRow());
    }

    private void displayErrorMessage(String message, boolean userIssue)
    {
        if (userIssue)
            JOptionPane.showMessageDialog(new JDialog(), message + "\r\nPlease correct this issue and then try again.", "Remove Error", JOptionPane.ERROR_MESSAGE);
        else
            JOptionPane.showMessageDialog(new JDialog(), message + "\r\nPlease contact an administrator.", "Remove Error", JOptionPane.ERROR_MESSAGE);
    }
}
