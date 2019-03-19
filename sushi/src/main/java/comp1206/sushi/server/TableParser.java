package comp1206.sushi.server;

import comp1206.sushi.common.*;

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

        if (data.size() <= 0)
        {
            return;
        }

        TableData tableData = new TableData(server);
        tableData.loadData(data, model);

        loadedData.put(type, data);
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
                    System.err.println(ex);
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
                    System.err.println(ex);
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
                    System.err.println(ex);
                    return false;
                }
                break;
        }

        return true;
    }
}
