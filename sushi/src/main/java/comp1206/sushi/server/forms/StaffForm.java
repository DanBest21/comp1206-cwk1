package comp1206.sushi.server.forms;

import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;

public class StaffForm extends EntryForm
{
    public StaffForm(ServerInterface server, TableView view, String formName)
    {
        super(server, view, formName);
        generateForm();
        setVisible(true);
    }

    @Override
    public void generateForm()
    {
        JTextField textName = createTextField("Staff Name");
        JButton btnSubmit = createSubmitButton();
        btnSubmit.addActionListener(e -> submitEntry(textName.getText()));
        resizeWindow();
    }

    private void submitEntry(String name)
    {
        getServer().addStaff(name);
        getTableView().updateTable("Staff");
        this.dispose();
    }
}
