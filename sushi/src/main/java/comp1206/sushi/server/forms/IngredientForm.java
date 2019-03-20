package comp1206.sushi.server.forms;

import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;

public class IngredientForm extends EntryForm
{
    public IngredientForm(ServerInterface server, TableView view, String formName)
    {
        super(server, view, formName);
    }

    @Override
    public void generateForm()
    {
        JTextField textIngredient = createTextField("Ingredient");
        JTextField textUnits = createTextField("Units");
        JComboBox comboBoxSupplier = createComboBox("Supplier", "Suppliers");
        JSpinner spinnerThreshold = createSpinner("Restock Threshold", 0, 0, 100, 1);
        JSpinner spinnerAmount = createSpinner("Restock Amount", 0, 0, 100, 1);
        JButton btnSubmit = createSubmitButton();
        btnSubmit.addActionListener(e -> submitEntry(textIngredient.getText(), textUnits.getText(),
                (Supplier)comboBoxSupplier.getSelectedItem(),
                (Integer)spinnerThreshold.getValue(), (Integer)spinnerAmount.getValue()));
        resizeWindow();
    }

    private void submitEntry(String ingredient, String units, Supplier supplier, Number restockThreshold, Number restockAmount)
    {
        getServer().addIngredient(ingredient, units, supplier, restockThreshold, restockAmount);
        getTableView().updateTable("Ingredients");
        this.dispose();
    }
}
