package comp1206.sushi.server.forms;

import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Ingredient;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.components.RecipeBuilder;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DishForm extends EntryForm
{
    private Dish dish = null;
    private static final double ADD_PERCENTAGE = 0.7;
    private static final double EDIT_PERCENTAGE = 0.8;

    public DishForm(ServerInterface server, TableView view, String formName)
    {
        super(server, view, formName);
        generateForm();
        setVisible(true);
    }

    public DishForm(ServerInterface server, TableView view, String formName, Dish dish)
    {
        super(server, view, formName);
        this.dish = dish;
        generateForm();
        setVisible(true);
    }

    @Override
    public void generateForm()
    {
        if (dish != null)
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
        JTextField textDish = createTextField("Dish Name");
        JTextArea textDescription = createTextArea("Description", 3);
        JFormattedTextField textCurrency = createCurrencyField("Price");
        RecipeBuilder compRecipe = createRecipeBuilder();
        JSpinner spinnerAmount = createSpinner("Restock Amount", 0, 0, 100, 1);
        JSpinner spinnerThreshold = createSpinner("Restock Threshold", 0, 0, 100, 1);
        JSpinner spinnerStock = createSpinner("Stock", 0, 0, 100, 1);
        JButton btnSubmit = createSubmitButton();
        btnSubmit.addActionListener(e -> submitEntry(textDish.getText(), textDescription.getText(),
                Double.parseDouble(textCurrency.getText().substring(1)), (Integer)spinnerAmount.getValue(),
                (Integer)spinnerThreshold.getValue(), (Integer)spinnerStock.getValue(), compRecipe.getRecipe()));
        resizeWindow(ADD_PERCENTAGE);
    }

    private void generateEditForm()
    {
        RecipeBuilder compRecipe = createRecipeBuilder();
        compRecipe.setRecipe(dish.getRecipe());
        JSpinner spinnerAmount = createSpinner("Restock Amount", 0, 0, 100, 1);
        spinnerAmount.setValue(dish.getRestockAmount());
        JSpinner spinnerThreshold = createSpinner("Restock Threshold", 0, 0, 100, 1);
        spinnerThreshold.setValue(dish.getRestockThreshold());
        JSpinner spinnerStock = createSpinner("Stock", 0, 0, 100, 1);
        spinnerStock.setValue(getServer().getDishStockLevels().get(dish));
        JButton btnSubmit = createSubmitButton();
        btnSubmit.addActionListener(e -> editEntry(dish, (Integer)spinnerThreshold.getValue(), (Integer)spinnerAmount.getValue(),
                (Integer)spinnerStock.getValue(), compRecipe.getRecipe()));
        resizeWindow(EDIT_PERCENTAGE);
    }

    private void submitEntry(String name, String description, Number price, Number restockThreshold, Number restockAmount, Number stock, Map<Ingredient, Number> recipe)
    {
        if (!validateInput(name, description, price, restockThreshold, restockAmount, stock, recipe))
            return;

        Dish dish = getServer().addDish(name, description, price, restockThreshold, restockAmount);
        getServer().setStock(dish, stock);
        getServer().setRecipe(dish, recipe);
        updateEntry();
    }

    private void editEntry(Dish dish, Number restockThreshold, Number restockAmount, Number stock, Map<Ingredient, Number> recipe)
    {
        getServer().setRestockLevels(dish, restockThreshold, restockAmount);
        getServer().setStock(dish, stock);
        getServer().setRecipe(dish, recipe);
        updateEntry();
    }

    private void updateEntry()
    {
        getTableView().updateTable("Dishes");
        this.dispose();
    }

    private RecipeBuilder createRecipeBuilder()
    {
        createLabel("Recipe");

        RecipeBuilder recipeBuilder = new RecipeBuilder(this, getTableView());

        setGridBagConstraints(recipeBuilder, 1, getNoRows(), 1, GridBagConstraints.EAST);
        addToContentPane(recipeBuilder);

        setNoRows(getNoRows() + 1);

        return recipeBuilder;
    }

    public void resizeForm(int noRowsChanged)
    {
        setNoRows(getNoRows() + noRowsChanged);
        resizeWindow((dish != null) ? EDIT_PERCENTAGE : ADD_PERCENTAGE);
    }

    private boolean validateInput(String name, String description, Number price, Number restockThreshold, Number restockAmount, Number stock, Map<Ingredient, Number> recipe)
    {
        if (name.trim().isEmpty() || description.trim().isEmpty())
        {
            throwMissingFieldsInputError();
            return false;
        }

        for (Dish dish : getServer().getDishes())
        {
            if (dish.getName().trim().equals(name.trim()))
            {
                throwDuplicateUniqueFieldError("Dish Name", name);
                return false;
            }
        }

        return true;
    }
}
