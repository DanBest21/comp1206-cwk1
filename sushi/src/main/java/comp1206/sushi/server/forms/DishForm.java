package comp1206.sushi.server.forms;

import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;

public class DishForm extends EntryForm
{
    private Dish dish = null;

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
//            generateEditForm();
        }
        else
        {
//            generateAddForm();
        }
    }
//
//    private void generateAddForm()
//    {
//        JTextField textIngredient = createTextField("Ingredient");
//        JTextField textUnits = createTextField("Units");
//        JComboBox comboBoxSupplier = createComboBox("Supplier", "Suppliers");
//        JSpinner spinnerThreshold = createSpinner("Restock Threshold", 0, 0, 100, 1);
//        JSpinner spinnerAmount = createSpinner("Restock Amount", 0, 0, 100, 1);
//        JButton btnSubmit = createSubmitButton();
//        btnSubmit.addActionListener(e -> submitEntry(textIngredient.getText(), textUnits.getText(),
//                (Supplier)comboBoxSupplier.getSelectedItem(),
//                (Integer)spinnerThreshold.getValue(), (Integer)spinnerAmount.getValue()));
//        resizeWindow();
//    }
//
//    private void generateEditForm()
//    {
//        JSpinner spinnerThreshold = createSpinner("Restock Threshold", 0, 0, 100, 1);
//        spinnerThreshold.setValue(ingredient.getRestockThreshold());
//        JSpinner spinnerAmount = createSpinner("Restock Amount", 0, 0, 100, 1);
//        spinnerAmount.setValue(ingredient.getRestockAmount());
//        JButton btnSubmit = createSubmitButton();
//        btnSubmit.addActionListener(e -> editEntry(ingredient, (Integer)spinnerThreshold.getValue(), (Integer)spinnerAmount.getValue()));
//        resizeWindow();
//    }
//
//    private void submitEntry(String ingredient, String units, Supplier supplier, Number restockThreshold, Number restockAmount)
//    {
//        getServer().addIngredient(ingredient, units, supplier, restockThreshold, restockAmount);
//        updateEntry();
//    }
//
//    private void editEntry(Ingredient ingredient, Number restockThreshold, Number restockAmount)
//    {
//        getServer().setRestockLevels(ingredient, restockThreshold, restockAmount);
//        updateEntry();
//    }
//
//    private void updateEntry()
//    {
//        getTableView().updateTable("Ingredients");
//        this.dispose();
//    }
}
