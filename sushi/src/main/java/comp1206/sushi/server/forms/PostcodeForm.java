package comp1206.sushi.server.forms;

import comp1206.sushi.server.ServerInterface;
import comp1206.sushi.server.table.TableView;

import javax.swing.*;

public class PostcodeForm extends EntryForm
{
    public PostcodeForm(ServerInterface server, TableView view, String formName)
    {
        super(server, view, formName);
    }

    @Override
    public void generateForm()
    {
        JTextField textPostcode = createTextField("Postcode");
        JButton btnSubmit = createSubmitButton();
        btnSubmit.addActionListener(e -> submitEntry(textPostcode.getText()));
        resizeWindow();
    }

    private void submitEntry(String postcode)
    {
        getServer().addPostcode(postcode);
        getTableView().updateTable("Postcodes");
        this.dispose();
    }
}

