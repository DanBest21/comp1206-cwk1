package comp1206.sushi.server.components;

import comp1206.sushi.common.Ingredient;
import comp1206.sushi.server.configuration.ServerConfiguration;
import comp1206.sushi.server.forms.DishForm;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

// RecipeBuilder class - Daniel Best, 2019
public class RecipeBuilder extends JPanel
{
    private List<Component[]> rows = new ArrayList<>();
    private GridBagLayout layout = new GridBagLayout();
    private GridBagConstraints constraints = new GridBagConstraints();
    private TableView view;
    private DishForm form;
    private int noRows = 0;
    private JButton btnAdd;
    private List<Ingredient> selectedIngredients = new ArrayList<>();

    private final static int[] SPINNER_PARAMETERS = new int[]{1, 1, 100, 1};

    public RecipeBuilder(DishForm form, TableView view)
    {
        this.form = form;
        this.view = view;
        setLayout(layout);
        setBackground(ServerConfiguration.getColour());
        constraints.insets = new Insets(5, 5, 5, 5);

        buildRow();

        btnAdd = new JButton("Add Ingredient");
        btnAdd.setFont(ServerConfiguration.getSmallTitleFont());
        btnAdd.addActionListener(e -> buildRow());

        setGridBagConstraints(btnAdd, 0, noRows + 1, 3, GridBagConstraints.CENTER);
        add(btnAdd);
    }

    private void buildRow()
    {
        Component[] row;
        int i = 0;

        if (noRows >= 1)
        {
            row = new Component[3];

            JButton btn = new JButton("X");
            btn.setFont(ServerConfiguration.getSmallTitleFont());

            setGridBagConstraints(btn, 0, noRows, 1, GridBagConstraints.EAST);
            final int index = noRows;
            btn.addActionListener(e -> deleteRow(index));
            add(btn);
            row[i++] = btn;
        }
        else
        {
            row = new Component[2];
        }

        SpinnerModel model = new SpinnerNumberModel(SPINNER_PARAMETERS[0], SPINNER_PARAMETERS[1], SPINNER_PARAMETERS[2], SPINNER_PARAMETERS[3]);
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(ServerConfiguration.getFont());

        setGridBagConstraints(spinner, 1, noRows, 1, GridBagConstraints.WEST);
        add(spinner);
        row[i++] = spinner;

        JComboBox comboBox = new JComboBox();

        loadComboBoxItems(comboBox);

        // *******************************************************************************************************
        // * Title: Combo Box with Custom Renderer
        // * Author: Rob Camick
        // * Date: 2013
        // * Availability: https://tips4java.wordpress.com/2013/11/17/combo-box-with-custom-renderer/
        // ******************************************************************************************************/
        comboBox.setRenderer(new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Ingredient)
                {
                    Ingredient ingredient = (Ingredient)value;
                    setText(ingredient.getName());
                }

                return this;
            }
        });

        comboBox.setFont(ServerConfiguration.getFont());
        comboBox.addActionListener(e -> {
            selectedIngredients.clear();

            for (int j = 0; j < rows.size(); j++)
            {
                JComboBox comboBox2 = (JComboBox) rows.get(j)[(j == 0) ? 1 : 2];

                if (!selectedIngredients.contains(comboBox2.getSelectedItem()))
                {
                    selectedIngredients.add((Ingredient)comboBox2.getSelectedItem());
                }
            }

            updateComboBoxItems(false);
        });

        setGridBagConstraints(comboBox, 2, noRows, 1, GridBagConstraints.EAST);
        add(comboBox);

        rows.add(row);
        row[i++] = comboBox;
        noRows++;

        if (comboBox.getItemCount() == 1)
        {
            hideAddButton();
            form.resizeForm(1);
        }
        else if (noRows > 1)
        {
            moveAddButton();
            form.resizeForm(1);
        }

        updateComboBoxItems(true);
    }

    private void deleteRow(int index)
    {
        JComboBox comboBox = (JComboBox) rows.get(index)[(index == 0) ? 1 : 2];

        for (Component component : rows.get(index))
        {
            remove(component);
        }

        selectedIngredients.remove(comboBox.getSelectedItem());

        rows.remove(index);
        noRows--;

        updateButtonListeners();
        updateComboBoxItems(false);
        repositionRows(index);

        form.resizeForm(-1);
        moveAddButton();

        if (!btnAdd.isVisible())
            showAddButton();
    }

    private void updateButtonListeners()
    {
        for (int i = 1; i < rows.size(); i++)
        {
            JButton btn = (JButton)rows.get(i)[0];
            btn.removeActionListener(btn.getActionListeners()[0]);
            final int newIndex = i;
            btn.addActionListener(e -> deleteRow(newIndex));
        }
    }

    private void repositionRows(int index)
    {
        for (int i = index; i < rows.size(); i++)
        {
            int j = 0;

            for (Component component : rows.get(i))
            {
                remove(component);
                setGridBagConstraints(component, j, i, 1, GridBagConstraints.CENTER);
                j++;
                add(component);
            }
        }
    }

    private void setGridBagConstraints(Component component, int x, int y, int width, int alignment)
    {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.anchor = alignment;

        layout.setConstraints(component, constraints);
    }

    private void moveAddButton()
    {
        remove(btnAdd);
        setGridBagConstraints(btnAdd, 0, noRows + 1, 3, GridBagConstraints.CENTER);
        add(btnAdd);
    }

    private void hideAddButton()
    {
        btnAdd.setVisible(false);
    }

    private void showAddButton()
    {
        btnAdd.setVisible(true);
    }

    public Map<Ingredient, Number> getRecipe()
    {
        Map<Ingredient, Number> recipe = new HashMap<>();

        for (Component[] row : rows)
        {
            int i = 1;

            if (row.length == 2)
                i--;

            JSpinner spinner = (JSpinner)row[i];
            JComboBox comboBox = (JComboBox)row[i + 1];

            recipe.put((Ingredient)comboBox.getSelectedItem(), (Integer)spinner.getValue());
        }

        return recipe;
    }

    public void setRecipe(Map<Ingredient, Number> recipe)
    {
        Iterator iterator = recipe.entrySet().iterator();
        int i = 0;

        while (iterator.hasNext())
        {
            Map.Entry entry = (Map.Entry)iterator.next();

            if (i == rows.size())
              buildRow();

            Component[] row = rows.get(i);
            int j = 1;

            if (row.length == 2)
                j--;

            JSpinner spinner = (JSpinner)row[j];
            JComboBox comboBox = (JComboBox)row[j + 1];

            spinner.setValue(entry.getValue());
            comboBox.setSelectedItem(entry.getKey());

            i++;
        }
    }

    private void updateComboBoxItems(boolean justAdded)
    {
        for (int i = 0; i < rows.size(); i++)
        {
            JComboBox comboBox = (JComboBox)rows.get(i)[(i == 0) ? 1 : 2];

            for (int j = 0; j < comboBox.getItemCount(); j++)
            {
                if (selectedIngredients.contains(comboBox.getItemAt(j)) && comboBox.getSelectedItem() != comboBox.getItemAt(j))
                {
                    comboBox.removeItemAt(j);
                }
            }

            if (!justAdded)
                loadComboBoxItems(comboBox);
        }
    }

    private void loadComboBoxItems(JComboBox comboBox)
    {
        List<Ingredient> items = (List<Ingredient>)view.getParser().getData("Ingredients");

        List<Ingredient> loadedItems = new ArrayList<>();

        for (int i = 0; i < comboBox.getItemCount(); i++)
        {
            loadedItems.add((Ingredient)comboBox.getItemAt(i));
        }

        for (Ingredient ingredient : items)
        {
            if (!selectedIngredients.contains(ingredient) && !loadedItems.contains(ingredient))
                comboBox.addItem(ingredient);
        }

        if (!selectedIngredients.contains(comboBox.getSelectedItem()))
            selectedIngredients.add((Ingredient)comboBox.getSelectedItem());
    }
}
