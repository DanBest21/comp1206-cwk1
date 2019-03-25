package comp1206.sushi.server.forms;

import comp1206.sushi.common.Staff;
import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;

// StaffForm class - Daniel Best, 2019
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
        if (!validateInput(name))
            return;

        getServer().addStaff(name);
        getTableView().updateTable("Staff");
        this.dispose();
    }

    private boolean validateInput(String name)
    {
        if (name.trim().isEmpty())
        {
            throwMissingFieldsInputError();
            return false;
        }

        for (Staff staff : getServer().getStaff())
        {
            if (staff.getName().trim().equals(name.trim()))
            {
                throwDuplicateUniqueFieldError("Staff Name", name);
                return false;
            }
        }

        return true;
    }
}
