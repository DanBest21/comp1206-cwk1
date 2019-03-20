package comp1206.sushi.server.forms;

import comp1206.sushi.common.Postcode;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;

public class SupplierForm extends EntryForm
{
    public SupplierForm(ServerInterface server, TableView view, String formName)
    {
        super(server, view, formName);
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
        getServer().addSupplier(name, postcode);
        getTableView().updateTable("Suppliers");
        this.dispose();
    }
}
