package comp1206.sushi.server.table;

import comp1206.sushi.common.*;
import comp1206.sushi.server.ServerInterface;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Method;
import java.util.*;

public class TableData
{
    private static ServerInterface server;

    public TableData(ServerInterface server)
    {
        TableData.server = server;
    }

    public void loadData(List<? extends Model> data, DefaultTableModel model)
    {
        List<String> columns = generateColumns(data);

        if (data.get(0).getClass() == Dish.class || data.get(0).getClass() == Ingredient.class)
            columns.add("Stock");
        if (data.get(0).getClass() == Order.class)
            columns.add("Cost");

        columns.sort((col1, col2) ->
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

        for (Object row : rows) {
            model.addRow((String[])row);
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
                    continue;
                }

                if (columns.get(j).equals("Cost"))
                {
                    fields[j] = getCost((Order)data.get(i));
                    continue;
                }


                fields[j] = getObject(data.get(i), data.get(i).getClass(), columns.get(j));
            }

            rows.add(fields);
        }

        return rows;
    }

    private String getObject(Object object, Class<?> model, String column)
    {
        String field = "";

        for (Method method : model.getMethods())
        {
            if (method.getName().contains(column.replaceAll("\\s+|/", "")) && method.getName().contains("get"))
            {
                try
                {
                    Object output = method.invoke(model.cast(object));

                    if (column.equals("Recipe") || column.equals("Lat/Long"))
                    {
                        field = handleMapObject(column, (Map)output);
                    }
                    else if (column.equals("Price"))
                    {
                        field = String.format("£%.2f", Double.parseDouble(output.toString()));
                    }
                    else
                    {
                        field = (output == null ? null : output.toString());
                    }

                    break;
                }
                catch (ReflectiveOperationException ex)
                {
                    ex.printStackTrace();
                }
            }
        }

        return field;
    }

    private <S, T> String handleMapObject(String field, Map<S, T> map)
    {
        String formattedOutput = "<html>";
        Iterator iterator = map.entrySet().iterator();

        switch (field)
        {
            case "Recipe":
                while (iterator.hasNext())
                {
                    Map.Entry entry = (Map.Entry)iterator.next();
                    Ingredient key = (Ingredient)entry.getKey();
                    Number value = (Number)entry.getValue();
                    formattedOutput = formattedOutput + value.toString() + "x " + key.toString() + (iterator.hasNext() ? "<br>" : "");
                }
                break;

            case "Lat/Long":
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

    private String getStock(Dish dish)
    {
        return server.getDishStockLevels().get(dish).toString();
    }

    private String getStock(Ingredient ingredient)
    {
        return server.getIngredientStockLevels().get(ingredient).toString();
    }

    private String getCost(Order order) { return server.getOrderCost(order).toString(); }
}
