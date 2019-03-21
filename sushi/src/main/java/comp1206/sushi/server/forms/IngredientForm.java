package comp1206.sushi.server.forms;

import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;

public class IngredientForm extends EntryForm
{
    private Ingredient ingredient = null;

    public IngredientForm(ServerInterface server, TableView view, String formName)
    {
        super(server, view, formName);
        generateForm();
        setVisible(true);
    }

    public IngredientForm(ServerInterface server, TableView view, String formName, Ingredient ingredient)
    {
        super(server, view, formName);
        this.ingredient = ingredient;
        generateForm();
        setVisible(true);
    }

    @Override
    public void generateForm()
    {
        if (ingredient != null)
        {
            generateEditForm();
        }
        else
        {
            generateAddForm();
        }
    }

    private void generateAddForm()
    {
        JTextField textIngredient = createTextField("Ingredient");
        JSpinner spinnerAmount = createSpinner("Restock Amount", 0, 0, 100, 1);
        JSpinner spinnerThreshold = createSpinner("Restock Threshold", 0, 0, 100, 1);
        JSpinner spinnerStock = createSpinner("Stock", 0, 0, 100, 1);
        JComboBox comboBoxSupplier = createComboBox("Supplier", "Suppliers");
        JTextField textUnits = createTextField("Units");
        JButton btnSubmit = createSubmitButton();
        btnSubmit.addActionListener(e -> submitEntry(textIngredient.getText(), textUnits.getText(),
                (Supplier)comboBoxSupplier.getSelectedItem(),
                (Integer)spinnerThreshold.getValue(), (Integer)spinnerAmount.getValue(), (Integer)spinnerStock.getValue()));
        resizeWindow();
    }

    private void generateEditForm()
    {
        JSpinner spinnerAmount = createSpinner("Restock Amount", 0, 0, 100, 1);
        spinnerAmount.setValue(ingredient.getRestockAmount());
        JSpinner spinnerThreshold = createSpinner("Restock Threshold", 0, 0, 100, 1);
        spinnerThreshold.setValue(ingredient.getRestockThreshold());
        JSpinner spinnerStock = createSpinner("Stock", 0, 0, 100, 1);
        spinnerStock.setValue(getServer().getIngredientStockLevels().get(ingredient));
        JButton btnSubmit = createSubmitButton();
        btnSubmit.addActionListener(e -> editEntry(ingredient, (Integer)spinnerThreshold.getValue(), (Integer)spinnerAmount.getValue(), (Integer)spinnerStock.getValue()));
        resizeWindow();
    }

    private void submitEntry(String ingredient, String units, Supplier supplier, Number restockThreshold, Number restockAmount, Number stock)
    {
        Ingredient ingredientObject = getServer().addIngredient(ingredient, units, supplier, restockThreshold, restockAmount);
        getServer().setStock(ingredientObject, stock);
        updateEntry();
    }

    private void editEntry(Ingredient ingredient, Number restockThreshold, Number restockAmount, Number stock)
    {
        getServer().setRestockLevels(ingredient, restockThreshold, restockAmount);
        getServer().setStock(ingredient, stock);
        updateEntry();
    }

    private void updateEntry()
    {
        getTableView().updateTable("Ingredients");
        this.dispose();
    }
}
