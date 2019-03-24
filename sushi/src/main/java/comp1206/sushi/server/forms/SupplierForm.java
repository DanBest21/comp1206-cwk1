package comp1206.sushi.server.forms;

import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;

public class SupplierForm extends EntryForm
{
    public SupplierForm(ServerInterface server, TableView view, String formName)
    {
        super(server, view, formName);
        generateForm();
        setVisible(true);
    }

    @Override
    public void generateForm()
    {
        JTextField textName = createTextField("Supplier Name");
        JComboBox comboBoxPostcode = createComboBox("Postcode", "Postcodes");
        JButton btnSubmit = createSubmitButton();
        btnSubmit.addActionListener(e -> submitEntry(textName.getText(), (Postcode)comboBoxPostcode.getSelectedItem()));
        resizeWindow();
    }

    private void submitEntry(String name, Postcode postcode)
    {
        if (!validateInput(name, postcode))
            return;

        getServer().addSupplier(name, postcode);
        getTableView().updateTable("Suppliers");
        this.dispose();
    }

    private boolean validateInput(String name, Postcode postcode)
    {
        if (name.trim().isEmpty())
        {
            throwMissingFieldsInputError();
            return false;
        }

        for (Supplier supplier : getServer().getSuppliers())
        {
            if (supplier.getName().trim().equals(name.trim()))
            {
                throwDuplicateUniqueFieldError("Supplier Name", name);
                return false;
            }
        }

        return true;
    }
}
