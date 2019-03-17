package comp1206.sushi.server;

import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Model;
import comp1206.sushi.mock.MockServer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

public class TableView extends JTabbedPane
{
    private HashMap<String, JScrollPane> tabs = new HashMap<>();
    private JTable table;
    private Color red = new Color(170, 50, 50);
    private Font titleFont = new Font("Viner Hand ITC", Font.BOLD, 20);
    private Font font = new Font("Courier New", Font.PLAIN, 16);

    public TableView(String[] tabs)
    {
        for (String tab : tabs)
        {
            JScrollPane table = generateTable(tab);
            this.tabs.put(tab, table);
            this.addTab(tab, this.tabs.get(tab));

            this.setOpaque(false);
            this.setFont(titleFont.deriveFont((float)16).deriveFont(Font.PLAIN));
        }
    }

    private JScrollPane generateTable(String tab)
    {
        DefaultTableModel model = new DefaultTableModel();
        table = new JTable(model);

        table.setFont(font);
        table.setRowHeight(16);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setOpaque(false);
        tableHeader.setBackground(red);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(titleFont);

        loadData(tab, model);

        table.setDefaultEditor(Object.class, null);

        JScrollPane pane = new JScrollPane(table);
        pane.setLayout(new ScrollPaneLayout());
        pane.setMinimumSize(new Dimension(800, 400));
        pane.setBackground(Color.WHITE);
        pane.getViewport().setBackground(Color.WHITE);

        return pane;
    }

    private void loadData(String type, DefaultTableModel model)
    {
        // TODO: Replace with actual server when created.
        ServerInterface server = new MockServer();
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

            case "Configuration":
                // TODO: Implement Configuration case.
                break;
        }

        if (data.size() <= 0)
        {
            return;
        }

        List<String> columns = generateColumns(data);
        Collections.sort(columns, (o1, o2) -> {
            if (o1.equals(o2)) // update to make it stable
                return 0;
            if (o1.equals("Name"))
                return -1;
            if (o2.equals("Name"))
                return 1;
            return o1.compareTo(o2);
        });

        for (String column : columns)
        {
            model.addColumn(column);
        }

        Map<Integer, List<String>> rows = populateRows(columns, data);
        Iterator iterator = rows.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry)iterator.next();
            List<String> row = (List<String>)entry.getValue();
            model.addRow(row.toArray());
        }

        formatTable();
    }

    private List<String> generateColumns(List<? extends Model> data)
    {
        List<String> columns = new ArrayList<>();

        for (Method method : data.get(0).getClass().getMethods())
        {
            if (method.getName().contains("get") && method.getName() != "getClass")
            {
                String column = method.getName().substring(3);

                int i = 0;
                StringBuilder sb = new StringBuilder();

                sb.append(column);

                for (char c : column.toCharArray())
                {
                    if (Character.isUpperCase(c) && i != 0)
                    {
                        if (column.equals("LatLong"))
                        {
                            sb.insert(i, "/");
                        }
                        else
                        {
                            sb.insert(i, " ");
                        }
                    }

                    i++;
                }

                columns.add(sb.toString());
            }
        }

        return columns;
    }

    private Map<Integer, List<String>> populateRows(List<String> columns, List<? extends Model> data)
    {
        Map<Integer, List<String>> rows = new HashMap<>(data.size());

        for (int i = 0; i < data.size(); i++)
        {
            int j = 0;
            List<String> fields = new ArrayList<>(columns.size());

            for (Method method : data.get(i).getClass().getMethods())
            {
                if (method.getName().contains(columns.get(j).replaceAll("\\s+|/","")) && method.getName().contains("get"))
                {
                    try
                    {
                        Object output = method.invoke(data.get(i));

                        if (columns.get(j).equals("Recipe") || columns.get(j).equals("Lat/Long"))
                        {
                            String formattedOutput = handleMapObject(columns.get(j), (Map)output);
                            fields.add(formattedOutput);
                        }
                        else
                        {
                            fields.add(output == null ? null : output.toString());
                        }
                        j++;
                    }
                    catch (ReflectiveOperationException ex)
                    {
                        System.err.println(ex);
                    }
                }

                if (j == columns.size())
                {
                    break;
                }
            }

            rows.put(i, fields);
        }

        return rows;
    }

    private String handleMapObject(String field, Map map)
    {
        String formattedOutput = "<html>";
        Iterator iterator;

        switch (field)
        {
            case "Recipe":
                Map<Ingredient, Number> recipe = map;
                iterator = recipe.entrySet().iterator();

                while (iterator.hasNext())
                {
                    Map.Entry entry = (Map.Entry)iterator.next();
                    Ingredient key = (Ingredient)entry.getKey();
                    Number value = (Number)entry.getValue();
                    formattedOutput = formattedOutput + value.toString() + "x " + key.toString() + (iterator.hasNext() ? "<br>" : "");
                }
                break;

            case "Lat/Long":
                Map<String, Double> latlong = map;
                iterator = latlong.entrySet().iterator();

                while (iterator.hasNext())
                {
                    Map.Entry entry = (Map.Entry)iterator.next();
                    String key = (String)entry.getKey();
                    Double value = (Double)entry.getValue();
                    formattedOutput = formattedOutput + (key == "lon" ? "Longitude" : "Latitude") + ": " + value.toString() + (iterator.hasNext() ? "<br>" : "");
                }
                break;
        }

        formattedOutput = formattedOutput + "</html>";

        return formattedOutput;
    }

    private void formatTable()
    {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setVerticalAlignment(SwingConstants.NORTH);

        for (int row = 0; row < table.getRowCount(); row++)
        {
            int rowHeight = table.getRowHeight();

            for (int column = 0; column < table.getColumnCount(); column++)
            {
                table.getColumnModel().getColumn(column).setCellRenderer(renderer);

                Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            table.setRowHeight(row, rowHeight);
        }
    }
}
