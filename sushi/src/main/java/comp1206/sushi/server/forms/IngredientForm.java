package comp1206.sushi.server.forms;

import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;

// IngredientForm class - Daniel Best, 2019
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
        resizeWindow(0.7);
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
        resizeWindow(0.85);
    }

    private void submitEntry(String ingredient, String units, Supplier supplier, Number restockThreshold, Number restockAmount, Number stock)
    {
        if (!validateInput(ingredient, units, supplier, restockThreshold, restockAmount, stock))
            return;

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

    private boolean validateInput(String ingredient, String units, Supplier supplier, Number restockThreshold, Number restockAmount, Number stock)
    {
        if (ingredient.trim().isEmpty() || units.trim().isEmpty())
        {
            throwMissingFieldsInputError();
            return false;
        }

        for (Ingredient ingredientObject : getServer().getIngredients())
        {
            if (ingredientObject.getName().trim().equals(ingredient.trim()))
            {
                throwDuplicateUniqueFieldError("Ingredient Name", ingredient);
                return false;
            }
        }

        return true;
    }
}
