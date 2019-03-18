package comp1206.sushi.server;

import com.sun.security.ntlm.Server;
import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Model;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Method;
import java.util.*;

public class TableData
{
    private static ServerInterface server;

    public TableData(ServerInterface server)
    {
        this.server = server;
    }

    public void loadData(List<? extends Model> data, DefaultTableModel model)
    {
        List<String> columns = generateColumns(data);

        if (data.get(0).getClass() == Dish.class || data.get(0).getClass() == Ingredient.class)
            columns.add("Stock");

        Collections.sort(columns, (col1, col2) ->
        {
            if (col1.equals(col2))
                return 0;
            if (col1.equals("Name"))
                return -1;
            if (col2.equals("Name"))
                return 1;
            return col1.compareTo(col2);
        });

        for (String column : columns)
        {
            model.addColumn(column);
        }

        List<String[]> rows = populateRows(columns, data);
        Iterator iterator = rows.iterator();

        while (iterator.hasNext())
        {
            String[] row = (String[]) iterator.next();
            model.addRow(row);
        }
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

    private List<String[]> populateRows(List<String> columns, List<? extends Model> data)
    {
        List<String[]> rows = new ArrayList<>(data.size());

        for (int i = 0; i < data.size(); i++)
        {
            String[] fields = new String[columns.size()];

            for (int j = 0; j < columns.size(); j++)
            {
                if (columns.get(j).equals("Stock"))
                {
                    if (data.get(i).getClass() == Dish.class)
                        fields[j] = getStock((Dish)data.get(i));
                    else
                        fields[j] = getStock((Ingredient)data.get(i));
                    break;
                }

                for (Method method : data.get(i).getClass().getMethods())
                {
                    if (method.getName().contains(columns.get(j).replaceAll("\\s+|/", "")) && method.getName().contains("get"))
                    {
                        try
                        {
                            Object output = method.invoke(data.get(i));

                            if (columns.get(j).equals("Recipe") || columns.get(j).equals("Lat/Long"))
                            {
                                String formattedOutput = handleMapObject(columns.get(j), (Map)output);
                                fields[j] = formattedOutput;
                            }
                            else if (columns.get(j).equals("Price"))
                            {
                                fields[j] = String.format("£%.2f", Double.parseDouble(output.toString()));
                            }
                            else
                            {
                                fields[j] = (output == null ? null : output.toString());
                            }

                            break;
                        }
                        catch (ReflectiveOperationException ex)
                        {
                            System.err.println(ex);
                        }
                    }
                }
            }

            rows.add(fields);
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

    public String getStock(Dish dish)
    {
        return server.getDishStockLevels().get(dish).toString();
    }

    public String getStock(Ingredient ingredient)
    {
        return server.getIngredientStockLevels().get(ingredient).toString();
    }
}
