package comp1206.sushi.server.table;

import comp1206.sushi.common.*;
import comp1206.sushi.server.ServerInterface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        {
            // TODO: Create an exception here.
            return;
        }

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
                // TODO: Implement Map case.
                break;
        }

        return data;
    }

    // TODO: Add validation for special cases.
    public boolean removeRow(String tab, JTable table)
    {
        List<? extends Model> objects = loadedData.get(tab);

        switch (tab)
        {
            case "Postcodes":
                try
                {
                    server.removePostcode((Postcode)objects.get(table.getSelectedRow()));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    ex.printStackTrace();
                    return false;
                }
                break;

            case "Drones":
                try
                {
                    server.removeDrone((Drone)objects.get(table.getSelectedRow()));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    ex.printStackTrace();
                    return false;
                }
                break;

            case "Staff":
                try
                {
                    server.removeStaff((Staff)objects.get(table.getSelectedRow()));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    ex.printStackTrace();
                    return false;
                }
                break;

            case "Suppliers":
                try
                {
                    server.removeSupplier((Supplier)objects.get(table.getSelectedRow()));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    ex.printStackTrace();
                    return false;
                }
                break;

            case "Ingredients":
                try
                {
                    server.removeIngredient((Ingredient) objects.get(table.getSelectedRow()));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    ex.printStackTrace();
                    return false;
                }
                break;

            case "Dishes":
                try
                {
                    server.removeDish((Dish) objects.get(table.getSelectedRow()));
                }
                catch (ServerInterface.UnableToDeleteException ex)
                {
                    ex.printStackTrace();
                    return false;
                }
                break;
        }

        return true;
    }
}
